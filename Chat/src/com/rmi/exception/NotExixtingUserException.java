package com.rmi.exception;

public class NotExixtingUserException extends Exception {
	private static final long serialVersionUID = 1L;

	private String exception = "Not Existing user";
	
	public NotExixtingUserException() {
		super();
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	
}
