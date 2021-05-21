package com.revature.autosurvey.users.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;


@Component
public class AwsSqsListener {

	private final QueueMessagingTemplate SQS;
	
	@Autowired
	public AwsSqsListener(QueueMessagingTemplate SQS) {
		this.SQS = SQS;
	}
		
	@SqsListener(value= "usersQueue.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveMessage(String stringJson) {
		// do some stuff
	}
		
		
//		try {
//			this.usersQueueUrl = this.sqs.getQue
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	@PostConstruct
//	public void continuousListener() {
//		Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
//			sqsAsyncClient.receiveMessage(ReceiveMessageRequest.builder()
//					.maxNumberOfMessages(5)
//					.queueUrl(usersQueueUrl)
//					.waitTimeSeconds(10)
//					.visibilityTimeout(30)
//					.build()
//			)
//		);
//		
//		receiveMessageResponseMono
//			.repeat()
//			.retry()
//			.map(ReceiveMessageResponse::messages)
//			.map(Flux::fromIterable)
//			.flatMap(messageFlux -> messageFlux)
//			.subscribe(message -> {
//				// do some stuff with the message
//				sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(usersQueueUrl).receiptHandle(message.receiptHandle()).build())
//					.thenAccept(deleteMessageResponse -> {
//						// log deleted message
//					});
//			});		
//	}
}
