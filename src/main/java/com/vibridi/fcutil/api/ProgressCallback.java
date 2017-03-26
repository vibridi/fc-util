package com.vibridi.fcutil.api;

@FunctionalInterface
public interface ProgressCallback {
	public void advance(double value);
}
