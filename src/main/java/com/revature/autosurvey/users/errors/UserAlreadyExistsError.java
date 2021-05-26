package com.revature.autosurvey.users.errors;

public class UserAlreadyExistsError extends Throwable {

	private static final long serialVersionUID = 8368094170496362206L;
	
	public UserAlreadyExistsError() {
		super();
	}
	
	public UserAlreadyExistsError(String errorMessage) {
		super(errorMessage);
	}

}
