package com.vibridi.fcutil.controller;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.engine.XLSXReader;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.exception.XLSXLoadException;
import com.vibridi.fxmlutils.FXMLUtils;
import com.vibridi.fxmlutils.controller.BaseController;

public class MainViewController extends BaseController {

	public MainViewController() {
	}
	
	public void loadWorkbooks() {
		List<File> files = FXMLUtils.fromFileSystemMultiple(stage, "xlsx");
		
		FCEngine engine = new FCEngine(files
				.stream()
				.map(XLSXReader::new)
				.collect(Collectors.toList()));
		
		try {
			engine.readOffers();
			engine.validateOffers();
			
		} catch (XLSXLoadException e) {
			FXMLUtils.errorAlert("Errore nel caricamento del file " + e.getMessage(), e);
		} catch(ValidatorException e) {
			FXMLUtils.errorAlert("File non valido", e.getMessage(), e);
		}
	}

}
