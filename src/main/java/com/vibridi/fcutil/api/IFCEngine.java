package com.vibridi.fcutil.api;

public interface IFCEngine {
	public void readOffers(ReadSuccessCallback successCallback, ReadErrorCallback errorCallback);
	public void validateOffers(ValidationSuccessCallback successCallback, ValidationErrorCallback errorCallback);
	public void computeLists();
	public void generateUnassignedPlayersList();
	public void generateContendedPlayersList();
}
