package com.vibridi.fcutil.utils;

public enum AppOptions {
	instance;

	private double roundBudget;
	private long offerCount;
	
	private AppOptions() {
		resetDefaults();
	}
	
	public void resetDefaults() {
		roundBudget = AppContext.instance.getDouble("default.budget");
		offerCount = AppContext.instance.getLong("default.offers");
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
	
	

}
