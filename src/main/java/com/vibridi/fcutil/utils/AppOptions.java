package com.vibridi.fcutil.utils;

public enum AppOptions {
	instance;

	private double roundBudget;
	private long offerCount;
	private boolean usingDefaultTableHeaders;
	private String[] headers;
	
	private AppOptions() {
		resetDefaults();
	}
	
	public void resetDefaults() {
		usingDefaultTableHeaders = true;
		roundBudget = AppContext.instance.getDouble("default.budget");
		offerCount = AppContext.instance.getLong("default.offers");
		headers = AppContext.instance.getString("default.headers").split(",");
	}

	public double getRoundBudget() {
		return roundBudget;
	}

	public void setRoundBudget(double roundBudget) {
		this.roundBudget = roundBudget;
	}

	public long getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(long offerCount) {
		this.offerCount = offerCount;
	}
	
	public boolean isUsingDefaultTableHeaders() {
		return usingDefaultTableHeaders;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setUsingDefaultTableHeaders(boolean usingDefaultTableHeaders) {
		this.usingDefaultTableHeaders = usingDefaultTableHeaders;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

}
