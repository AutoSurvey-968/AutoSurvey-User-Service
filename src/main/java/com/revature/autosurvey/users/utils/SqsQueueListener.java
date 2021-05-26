package com.revature.autosurvey.users.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.User.Role;
import org.springframework.stereotype.Component;
import com.revature.autosurvey.users.security.FirebaseUtil;


@Component
public class SqsQueueListener {

	
	private SqsQueueSender queueSender;
	
	@Autowired
	public void setSqsQueueSender(SqsQueueSender queueSender) {
		this.queueSender = queueSender;
	}
	
	@SqsListener(value="usersQueue", deletionPolicy=SqsMessageDeletionPolicy.ON_SUCCESS)
	public void queueListener(Message<?> message) throws FirebaseAuthException {
		
		String returnQueue = message.getHeaders().get("returnQueue", String.class);
		String token = (String) message.getPayload();
		Boolean authorized;
		@SuppressWarnings("unchecked")
		List<Role> roles = (List<Role>) FirebaseAuth
			.getInstance()
			.verifyIdToken(token)
			.getClaims()
			.get("roles");
		
		if(roles.contains(User.Role.ROLE_USER)) {
			authorized = true;
		} else {authorized = false;}
		
		queueSender.send(
				returnQueue, 
				authorized.toString(), 
				message.getHeaders());
	}

	/*
	 queueListener listens to the usersQueue for authentication requests received from other services
	 the payload contains the cookie/custom firebase token, header contains sender info
	 Boolean for token authorization confirmation is instantiated
	 if the token contains the correct authorities, set authorized boolean to true, else false
	 put the original token as a message header and the auth boolean as the payload
	 use the send method to send the response to the appropriate queue
	 request is deleted from queue on success
	 */
}
