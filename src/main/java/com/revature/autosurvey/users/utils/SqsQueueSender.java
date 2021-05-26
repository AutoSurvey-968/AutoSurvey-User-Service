package com.revature.autosurvey.users.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSAsync;

//@Component
public class SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;

	//private final AmazonSQSAsync sqs;
	
	//@Autowired
	public SqsQueueSender(AmazonSQSAsync sqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(sqs);
	}
	
	public void send(String toQueue, Map<String, Boolean> message) {
		this.queueMessagingTemplate.send(toQueue, MessageBuilder.withPayload(message).build());
	}
	// send method will return the received token for confirmation purposes, along with a boolean verifying the user's authentication

}
