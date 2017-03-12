package com.vibridi.fcutil.validator;

import com.vibridi.fcutil.engine.XLSXReader;

public class BudgetValidator extends Validator<XLSXReader> {

	private double budget;
	
	public BudgetValidator(double budget) {
		this.budget = budget;
	}

	@Override
	public boolean isValid(XLSXReader target) {
		return target.sumOffers() <= budget;
	}

	@Override
	public String getMessage(XLSXReader target) {
		return "Il file " + target.getFileName() + 
				" ha superato il budget d'offerta (" + budget + " Euro)";
	}

}
