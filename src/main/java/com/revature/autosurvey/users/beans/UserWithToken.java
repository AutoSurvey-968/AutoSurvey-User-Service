package com.revature.autosurvey.users.beans;

import org.springframework.beans.BeanUtils;

public class UserWithToken extends User {

	private static final long serialVersionUID = -87467209632178467L;
	
	private String token;
	
	public UserWithToken() {
		super();
	}
	
	public UserWithToken(User u, String token) {
		BeanUtils.copyProperties(u, this);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
