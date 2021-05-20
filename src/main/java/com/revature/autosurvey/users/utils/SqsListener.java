package com.revature.autosurvey.users.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
public class SqsListener {

	private final SqsAsyncClient sqsAsyncClient;
	private final String usersQueueUrl;
	
	@Autowired
	public SqsListener(SqsAsyncClient sqsAsyncClient) {
		this.sqsAsyncClient = sqsAsyncClient;
		try {
			this.usersQueueUrl = this.sqsAsyncClient.getQueueUrl(GetQueueUrlRequest.builder().queueName("usersQueue.fifo").build()).get().queueUrl();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@PostConstruct
	public void continuousListener() {
		Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
			sqsAsyncClient.receiveMessage(ReceiveMessageRequest.builder()
					.maxNumberOfMessages(5)
					.queueUrl(usersQueueUrl)
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
				sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(usersQueueUrl).receiptHandle(message.receiptHandle()).build())
					.thenAccept(deleteMessageResponse -> {
						// log deleted message
					});
			});		
	}
}
