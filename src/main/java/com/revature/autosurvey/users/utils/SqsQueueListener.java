package com.revature.autosurvey.users.utils;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.amazonaws.util.json.Jackson;
import com.revature.autosurvey.users.security.FirebaseUtil;

@Component
public class SqsQueueListener {

	@Autowired
	private SqsQueueSender queueSender;

	@Autowired
	private FirebaseUtil firebaseUtil;
	
	private Logger log = LoggerFactory.getLogger(SqsQueueListener.class);

//	@SqsListener(value="usersQueue", deletionPolicy=SqsMessageDeletionPolicy.ON_SUCCESS)
//	public void queueListener(Map<String, String> payload) {
//		Optional<String> token = payload.keySet().stream().findFirst();
//		if (token.isPresent()) {
//			String responseQueue = payload.get(token.get());
//			firebaseUtil.getDetailsFromCustomToken(token.get()).doOnSuccess(verified -> {
//				Pair<String, Boolean> response = Pair.of(token.get(), Boolean.valueOf(verified != null));
//				queueSender.send(responseQueue, response);
//			});
//		}
//	}

//	@SqsListener(value = "usersQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	@SqsListener(value = "test", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void queueListener(@Payload String payload) {
//		String token = payload.getFirst();
//		String responseQueue = payload.getSecond();
//		firebaseUtil.getDetailsFromCustomToken(token).doOnSuccess(verified -> {
//			Pair<String, Boolean> response = Pair.of(token, Boolean.valueOf(verified != null));
//			queueSender.send(responseQueue, response);
//		});
		@SuppressWarnings("unchecked")
		Map<String, String> mip = Jackson.fromJsonString(payload, Map.class);
		log.debug(mip.keySet().toString());
		log.debug(mip.values().toString());
	}

	// queueListener listens to the usersQueue for authentication requests received
	// from other services
	// the token and response location are extracted from the passed map; Boolean
	// for auth confirmation is instantiated
	// response map that will contain the original token and a boolean confirming
	// authorization is instantiated
	// if the token contains the correct authorities, set authorized boolean to
	// true, else false
	// put the original token and response boolean in the outgoing map
	// use the send method to send the response map to the appropriate queue, delete
	// request from queue
}
