package com.vibridi.fcutil.exception;

public class XLSXWriteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public XLSXWriteException(String msg) {
		super(msg);
	}
	
	public XLSXWriteException(Throwable t) {
		super(t);
	}
	
	public XLSXWriteException(String msg, Throwable t) {
		super(msg, t);
	}

}
