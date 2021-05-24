package com.revature.autosurvey.users.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import com.revature.autosurvey.users.security.FirebaseUtil;

@Component
public class SqsQueueListener {

	@Autowired
	private SqsQueueSender queueSender;
	
	@Autowired
	private FirebaseUtil firebaseUtil;
	
	@SqsListener(value="usersQueue", deletionPolicy=SqsMessageDeletionPolicy.ON_SUCCESS)
	public void queueListener(Map<String, String> payload) {
		Optional<String> token = payload.keySet().stream().findFirst();
		if (token.isPresent()) {
			String responseQueue = payload.get(token.get());
			Map<String, Boolean> response = new HashMap<>();
			firebaseUtil.getDetailsFromCustomToken(token.get()).doOnSuccess(verified -> {
				response.put(token.get(), Boolean.valueOf(verified != null));
				queueSender.send(responseQueue, response);
			});
		}
	}

	// queueListener listens to the usersQueue for authentication requests received from other services
	// the token and response location are extracted from the passed map; Boolean for auth confirmation is instantiated
	// response map that will contain the original token and a boolean confirming authorization is instantiated
	// if the token contains the correct authorities, set authorized boolean to true, else false
	// put the original token and response boolean in the outgoing map
	// use the send method to send the response map to the appropriate queue, delete request from queue
}
