package com.vibridi.fcutil.api;

@FunctionalInterface
public interface ValidationErrorCallback {
	public void onError(int itemAt, Throwable t);
}
