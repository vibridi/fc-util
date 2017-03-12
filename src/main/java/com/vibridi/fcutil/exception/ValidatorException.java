package com.vibridi.fcutil.exception;

public class ValidatorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidatorException(String message) {
		super(message);
	}
	
	public ValidatorException(String message, Throwable t) {
		super(message, t);
	}

}
