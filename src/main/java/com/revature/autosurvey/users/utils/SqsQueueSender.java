package com.revature.autosurvey.users.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Component
public class SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	public SqsQueueSender(AmazonSQSAsync sqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(sqs);
	}
	
	public void send(String toQueue, String message, MessageHeaders headers) {
		this.queueMessagingTemplate.send(toQueue, 
				MessageBuilder
				.withPayload(message)
				.copyHeaders(headers)
				.build());
	}
}
