package com.vibridi.fcutil.engine;

import java.util.List;

import com.vibridi.fcutil.api.IFCEngine;
import com.vibridi.fcutil.utils.AppOptions;
import com.vibridi.fcutil.validator.BudgetValidator;
import com.vibridi.fcutil.validator.OfferCountValidator;
import com.vibridi.fcutil.validator.ValidatorChain;

public class FCEngine implements IFCEngine {

	private List<XLSXReader> readers;
	private ValidatorChain<XLSXReader> chain;
	
	public FCEngine(List<XLSXReader> readers) {
		this.readers = readers;
		
		chain = new ValidatorChain<XLSXReader>();
		chain.addValidator(new OfferCountValidator(AppOptions.instance.getOfferCount()));
		chain.addValidator(new BudgetValidator(AppOptions.instance.getRoundBudget()));
	}

	@Override
	public void readOffers() {
		readers.forEach(XLSXReader::load);
	}

	@Override
	public void validateOffers() {
		readers.forEach(chain::validate);
	}

	@Override
	public void computeLists() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateUnassignedPlayersList() {
		// TODO Auto-generated method stub
		
	}
	
}
