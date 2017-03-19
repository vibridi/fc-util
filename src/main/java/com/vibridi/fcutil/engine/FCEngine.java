package com.vibridi.fcutil.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.vibridi.fcutil.api.IFCEngine;
import com.vibridi.fcutil.api.ReadErrorCallback;
import com.vibridi.fcutil.api.ReadSuccessCallback;
import com.vibridi.fcutil.api.ValidationErrorCallback;
import com.vibridi.fcutil.api.ValidationSuccessCallback;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.exception.XLSXLoadException;
import com.vibridi.fcutil.model.Player;
import com.vibridi.fcutil.utils.AppOptions;
import com.vibridi.fcutil.validator.BudgetValidator;
import com.vibridi.fcutil.validator.DuplicateNamesValidator;
import com.vibridi.fcutil.validator.OfferCountValidator;
import com.vibridi.fcutil.validator.TableHeaderValidator;
import com.vibridi.fcutil.validator.ValidatorChain;

public class FCEngine implements IFCEngine {

	private List<XLSXReader> readers;
	private List<XLSXWriter> writers;
	private ValidatorChain<XLSXReader> chain;
	
	public FCEngine(List<XLSXReader> readers) {
		this.readers = readers;
		
		chain = new ValidatorChain<XLSXReader>();
		chain.addValidator(new TableHeaderValidator());
		chain.addValidator(new DuplicateNamesValidator());
		chain.addValidator(new OfferCountValidator(AppOptions.instance.getOfferCount()));
		chain.addValidator(new BudgetValidator(AppOptions.instance.getRoundBudget()));
	}

	@Override
	public void readOffers(ReadSuccessCallback successCallback, ReadErrorCallback errorCallback) {
		for(int i = 0; i < readers.size(); i++) {
			try {
				readers.get(i).load();
				if(successCallback != null)
					successCallback.onSuccess(i);
				Thread.sleep(AppOptions.instance.getThreadWaitMillis());
			} catch(XLSXLoadException | InterruptedException e) {
				if(errorCallback != null)
					errorCallback.onError(i,e);
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public void validateOffers(ValidationSuccessCallback successCallback, ValidationErrorCallback errorCallback) {
		for(int i = 0; i < readers.size(); i++) {
			try {
				chain.validate(readers.get(i));
				if(successCallback != null)
					successCallback.onSuccess(i);
				Thread.sleep(AppOptions.instance.getThreadWaitMillis());
			} catch(ValidatorException e) {
				if(errorCallback != null)
					errorCallback.onError(i,e);
				throw e;
			} catch(InterruptedException e) {
				if(errorCallback != null)
					errorCallback.onError(i,e);
				throw new IllegalStateException(e);
			}
		}	
	}

	@Override
	public void computeLists() {
		writers = 
		readers.stream()
			.map(reader -> reader.getPlayersMap())	// <name,Player>
			.flatMap(m -> m.entrySet().stream())
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, this::assignPlayer))
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
	
	@Override
	public void generateContendedPlayersList() {
		// TODO
	}
	
	private Player assignPlayer(Player p1, Player p2) {
		if(p1.getOffer() == p2.getOffer()) {	// contended player
			if(p1.getOffer() == 0.0) {			// unassigned
				p1.setOfferer("NonAssegnati.xlsx");
				return p1;
			}
			
			p1.setOfferer("GiocatoriContesi.xlsx");
			p1.addContender(p2.getOfferer());
			return p1;
		}
		
		return p1.getOffer() > p2.getOffer() ? p1 : p2;
	}
	
	private <T> List<T> newArrayList(T... elements) {
		List<T> list = new ArrayList<T>(Arrays.asList(elements));
		return list;
	}
}
