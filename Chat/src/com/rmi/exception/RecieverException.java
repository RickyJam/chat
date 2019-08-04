package com.rmi.exception;

public class RecieverException extends Exception {
	private static final long serialVersionUID = 1L;

	private String exception = "User Unreaceable!!!";
	
	public RecieverException() {
		super();
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	
}
