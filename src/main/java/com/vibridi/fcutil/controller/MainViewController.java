package com.vibridi.fcutil.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.engine.XLSXReader;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.exception.XLSXLoadException;
import com.vibridi.fcutil.utils.AppOptions;
import com.vibridi.fxmlutils.FXMLUtils;
import com.vibridi.fxmlutils.controller.BaseController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

public class MainViewController extends BaseController {

	private FCEngine engine;
	
	public MainViewController() {
	}
	
	@FXML
	public void initialize() {
		this.initializeListsTab();
		this.initializeOptionsTab();
	}
	
	@Override
	protected void handleCloseRequest(WindowEvent event) {		
	}
	
	
	/* **************************
	 * 							*
	 * 		   TAB LISTE		*
	 * 							*
	 * **************************/
	
	private void initializeListsTab() {
		
	}
	
	public void chooseSources() {
		List<File> files = FXMLUtils.fromFileSystemMultiple(stage, "xlsx");
		
		engine = new FCEngine(files
				.stream()
				.map(XLSXReader::new)
				.collect(Collectors.toList()));
	}
	
	public void readOffers() {
		try {
			engine.readOffers();
			engine.validateOffers();
			
		} catch (XLSXLoadException e) {
			FXMLUtils.errorAlert("Errore nel caricamento del file " + e.getMessage(), e);
		} catch(ValidatorException e) {
			FXMLUtils.errorAlert("File non valido", e.getMessage(), e);
		}
	}
	
	public void processLists() {
		engine.computeLists();
	}

	
	/* **************************
	 * 							*
	 * 		   TAB OPZIONI		*
	 * 							*
	 * **************************/
	
	@FXML private TextField columnNamesField;
	@FXML private TextField maxOffersField;
	@FXML private TextField budgetField;
	
	private void initializeOptionsTab() {
		columnNamesField.setText(Arrays.stream(AppOptions.instance.getHeaders())
				.collect(Collectors.joining(",")));
		maxOffersField.setText(Long.toString(AppOptions.instance.getOfferCount()));
		budgetField.setText(Double.toString(AppOptions.instance.getRoundBudget()));
		
		columnNamesField.textProperty().addListener((ob,ov,nv) -> AppOptions.instance.setHeaders(nv.split(",")));
		maxOffersField.textProperty().addListener((ob,ov,nv) -> AppOptions.instance.setOfferCount(Long.parseLong(nv)));
		budgetField.textProperty().addListener((ob,ov,nv) -> AppOptions.instance.setRoundBudget(Double.parseDouble(nv)));
	}
	
}
