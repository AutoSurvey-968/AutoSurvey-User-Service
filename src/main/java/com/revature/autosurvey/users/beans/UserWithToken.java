package com.revature.autosurvey.users.beans;

public class UserWithToken extends User {

	private static final long serialVersionUID = -87467209632178467L;
	
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
