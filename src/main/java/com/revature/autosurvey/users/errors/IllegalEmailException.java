package com.revature.autosurvey.users.errors;

public class IllegalEmailException extends IllegalArgumentException{
	
	private static final long serialVersionUID = 4579113955467868750L;

	public IllegalEmailException() {
		super();
	}
	
	public IllegalEmailException(String errorMessage) {
		super(errorMessage);
	}
}
