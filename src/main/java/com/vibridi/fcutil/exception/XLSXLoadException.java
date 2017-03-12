package com.vibridi.fcutil.exception;

public class XLSXLoadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public XLSXLoadException(String msg) {
		super(msg);
	}
	
	public XLSXLoadException(Throwable t) {
		super(t);
	}
	
	public XLSXLoadException(String msg, Throwable t) {
		super(msg, t);
	}

}
