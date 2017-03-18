package com.vibridi.fcutil.api;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public interface IFCEngine {
	public void readOffers() throws InvalidFormatException, IOException;
	public void validateOffers();
	public void computeLists();
	public void generateUnassignedPlayersList();
	public void generateContendedPlayersList();
}
