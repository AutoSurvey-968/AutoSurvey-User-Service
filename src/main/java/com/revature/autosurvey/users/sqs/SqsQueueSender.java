package com.revature.autosurvey.users.sqs;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.util.json.Jackson;

@Component
public class SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;
	
	@Value("${aws.queuename}")
	private String queueName;

	@Autowired
	public SqsQueueSender(AmazonSQSAsync sqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(sqs);
	}
	
	public void sendEmail(Map<String, String> message) {
		this.queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(Jackson.toJsonString(message)).build());
	}

}
