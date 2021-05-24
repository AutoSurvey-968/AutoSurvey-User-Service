package com.revature.autosurvey.users.errors;

public class IllegalPasswordException extends IllegalArgumentException{

	private static final long serialVersionUID = 3384376156948002834L;

	public IllegalPasswordException() {
		super();
	}
	
	public IllegalPasswordException(String errorMessage) {
		super(errorMessage);
	}
}
