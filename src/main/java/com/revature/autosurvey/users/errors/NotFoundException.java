package com.revature.autosurvey.users.errors;

public class NotFoundException extends Throwable {

	private static final long serialVersionUID = 8654814535437242330L;
	
	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
