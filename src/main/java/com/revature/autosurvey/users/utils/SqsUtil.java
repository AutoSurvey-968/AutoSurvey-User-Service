package com.revature.autosurvey.users.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Order(1)
public class SqsUtil implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		// intentionally blank	
	}
}

@Component
class SqsListener {

	private final SqsAsyncClient sqsAsyncClient;
	private final String userQueueUrl;
	
	@Autowired
	public SqsListener(SqsAsyncClient sqsAsyncClient) {
		this.sqsAsyncClient = sqsAsyncClient;
		try {
			this.userQueueUrl = this.sqsAsyncClient.getQueueUrl(GetQueueUrlRequest.builder().queueName("userQueue").build()).get().queueUrl();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@PostConstruct
	public void continuousListener() {
		Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
			sqsAsyncClient.receiveMessage(ReceiveMessageRequest.builder()
					.maxNumberOfMessages(5)
					.queueUrl(userQueueUrl)
					.waitTimeSeconds(10)
					.visibilityTimeout(30)
					.build()
			)
		);
		
		receiveMessageResponseMono
			.repeat()
			.retry()
			.map(ReceiveMessageResponse::messages)
			.map(Flux::fromIterable)
			.flatMap(messageFlux -> messageFlux)
			.subscribe(message -> {
				// do some stuff with the message
				sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(userQueueUrl).receiptHandle(message.receiptHandle()).build())
					.thenAccept(deleteMessageResponse -> {
						// log deleted message
					});
			});		
	}
}

@Component
class SqsSender {
	
	private final SqsAsyncClient sqsAsyncClient;
	
	@Autowired
	public SqsSender(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }
	
    @PostConstruct
    public void sendMessageRequest(String toQueue, String key, MessageAttributeValue value) throws Exception {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(key, value);
    	CompletableFuture<GetQueueUrlResponse> msg = sqsAsyncClient
        		.getQueueUrl(
        				GetQueueUrlRequest
        				.builder()
        				.queueName(toQueue)
        				.build());
        GetQueueUrlResponse getQueueUrlResponse = msg.get();

        Mono.fromFuture(() -> sqsAsyncClient.sendMessage(
                SendMessageRequest.builder()
                        .queueUrl(getQueueUrlResponse.queueUrl())
                        .messageAttributes(messageAttributes)
                        .build()
            ))
                .retryWhen(Retry.max(3))
                .repeat(5)
                .subscribe();
    }
}

		
