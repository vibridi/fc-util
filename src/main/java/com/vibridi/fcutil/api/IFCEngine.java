package com.vibridi.fcutil.api;

import java.io.File;

public interface IFCEngine {
	public void readOffers(ReadSuccessCallback successCallback, ReadErrorCallback errorCallback);
	public void validateOffers(ValidationSuccessCallback successCallback, ValidationErrorCallback errorCallback);
	public void computeLists();
	public void writeLists(final File targetDirectory);
}
