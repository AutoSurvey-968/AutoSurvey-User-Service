package com.revature.autosurvey.security;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.group3.beans.Gamer;
import com.revature.autosurvey.beans.User;

@Component
public class FirebaseUtil {
	
	public String generateToken(User user) throws FirebaseAuthException {
		Map<String, Object> claims =  new HashMap<>();
		claims.put("roles", user.getAuthorities().stream().map(auth -> auth.toString()).collect(Collectors.toList()));
		return FirebaseAuth.getInstance().createCustomToken(user.getId().toString(), claims);
	}
}
