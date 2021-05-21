package com.revature.autosurvey.users.errors;

public class UserAlreadyExistsException extends Throwable {

	private static final long serialVersionUID = 8368094170496362206L;
	
	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}

}
