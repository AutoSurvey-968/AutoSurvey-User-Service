package com.revature.autosurvey.users.beans;

import lombok.Data;

@Data
public class PasswordChangeRequest {
	private int userId;
	private String oldPass;
	private String newPass;
	
	public PasswordChangeRequest() {
		super();
	}
	
}
