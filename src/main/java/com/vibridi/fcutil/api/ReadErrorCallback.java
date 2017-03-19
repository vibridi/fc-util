package com.vibridi.fcutil.api;

@FunctionalInterface
public interface ReadErrorCallback {
	public void onError(int itemAt, Throwable exception);
}
