package com.revature.autosurvey.users.beans;

import lombok.Data;

@Data
public class TokenVerifierRequest {
	private String token;
	private boolean returnSecureToken;
	
	
	public TokenVerifierRequest() {
		super();
	}

	
}
