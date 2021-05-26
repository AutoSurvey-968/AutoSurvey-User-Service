package com.revature.autosurvey.users.errors;

public class NotFoundError extends Throwable {

	private static final long serialVersionUID = 8654814535437242330L;
	
	public NotFoundError() {
		super();
	}
	
	public NotFoundError(String errorMessage) {
		super(errorMessage);
	}
}
