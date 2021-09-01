package com.revature.autosurvey.users.beans;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	private String password;
	
	public LoginRequest() {
		super();
	}
	
	
}
