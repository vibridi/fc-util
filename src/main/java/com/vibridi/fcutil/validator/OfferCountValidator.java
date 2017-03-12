package com.vibridi.fcutil.validator;

import com.vibridi.fcutil.engine.XLSXReader;

public class OfferCountValidator extends Validator<XLSXReader> {

	private long required;
	private long actual;
	
	public OfferCountValidator(long required) {
		this.required = required;
	}
	
	@Override
	public boolean isValid(XLSXReader target) {
		return target.countOffers() == required;
	}
	
	@Override
	public String getMessage(XLSXReader target) {
		return "Il file " + target.getFileName() +
				" contiene " + (actual < required ? "meno" : "più") + 
				" offerte del consentito (" + required + ")" ;
	}

}
