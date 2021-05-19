package com.revature.autosurvey.beans;

public class TokenVerifier {
	private String token;
	private boolean returnSecureToken;
	
	public TokenVerifier() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isReturnSecureToken() {
		return returnSecureToken;
	}

	public void setReturnSecureToken(boolean returnSecureToken) {
		this.returnSecureToken = returnSecureToken;
	}
	
	
}
