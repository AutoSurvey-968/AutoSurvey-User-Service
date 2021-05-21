//package com.revature.autosurvey.users.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Component
//@Order(1)
//public class SqsUtil implements CommandLineRunner {
//	
//	@Autowired
//	private QueueMessagingTemplate queueMessagingTemplate;
//	
//	@Value("${cloud.aws.end-point.uri}")
//	private String endpoint;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		// intentionally blank
//	}
//}