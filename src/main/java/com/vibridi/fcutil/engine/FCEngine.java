package com.vibridi.fcutil.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vibridi.fcutil.api.IFCEngine;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppOptions;
import com.vibridi.fcutil.validator.BudgetValidator;
import com.vibridi.fcutil.validator.OfferCountValidator;
import com.vibridi.fcutil.validator.ValidatorChain;

public class FCEngine implements IFCEngine {

	private List<XLSXReader> readers;
	private List<XLSXWriter> writers;
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
		writers = 
		readers.stream()
			.map(reader -> reader.getPlayersMap())
			.flatMap(m -> m.entrySet().stream())
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (p1, p2) -> {
				Player pmax = p1.getOffer() > p2.getOffer() ? p1 : p2;
				Player pmin = pmax == p1 ? p2 : p1;
				pmax.setSold(true);
				pmin.setSold(false);
				return pmax;
			}))
			.values().stream()
			.collect(Collectors.toMap(Player::getOfferer, this::newArrayList, (o1, o2) -> { 
				o1.addAll(o2);
				return o1;
			}))
			.entrySet().stream()
			.map(entry -> { return new XLSXWriter(entry.getKey(), entry.getValue()); })
			.collect(Collectors.toList());
	}
	
	
	
	public List<XLSXWriter> getWriters() {
		return writers;
	}

	@Override
	public void generateUnassignedPlayersList() {
		// TODO Auto-generated method stub
		
	}
	
	private <T> List<T> newArrayList(T... elements) {
		List<T> list = new ArrayList<T>(Arrays.asList(elements));
		return list;
	}
	
}
