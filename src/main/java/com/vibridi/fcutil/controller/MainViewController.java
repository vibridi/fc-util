package com.vibridi.fcutil.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fcutil.concurrent.UITask;
import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.engine.XLSXReader;
import com.vibridi.fcutil.model.SourceFile;
import com.vibridi.fcutil.utils.AppMessages;
import com.vibridi.fcutil.utils.AppOptions;
import com.vibridi.fxmlutils.FXMLUtils;
import com.vibridi.fxmlutils.controller.BaseController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	
	@FXML private TableView<SourceFile> table;
	@FXML private TableColumn<SourceFile,String> nameCol;
	@FXML private TableColumn<SourceFile,Number> slotsCol;
	@FXML private TableColumn<SourceFile,Number> budgetCol;
	@FXML private TableColumn<SourceFile,String> statusCol;
	
	private void initializeListsTab() {
		nameCol.setCellValueFactory(cell -> cell.getValue().fileNameProperty());
		slotsCol.setCellValueFactory(cell -> cell.getValue().slotsProperty());
		budgetCol.setCellValueFactory(cell -> cell.getValue().budegtProperty());
		statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());
		statusCol.setCellFactory(callback -> { return new StatusTableCell(); });
	}
	
	@FXML
	public void chooseSources() {
		List<File> files = FXMLUtils.openFiles(stage, "xlsx");
		if(files == null || files.size() == 0)
			return;
		
		table.getItems().clear();
		files.stream()
			.map(this::newSourceFile)
			.collect(Collectors.collectingAndThen(Collectors.toList(), table.getItems()::addAll));
		
		engine = new FCEngine(files.stream()
					.map(XLSXReader::new)
					.collect(Collectors.toList()));
		
		new Thread(new UITask(this,engine)).start();
	}
	
	@FXML
	public void computeLists() {
		if(engine == null) {
			FXMLUtils.warningAlert(AppMessages.NO_FILE_LOADED).showAndWait();
			return;
		}
		
		boolean processable = true;
		for(SourceFile sf : table.getItems()) {
			if(sf.getError() != null)
				processable = false;
		}
		
		if(!processable) {
			FXMLUtils.warningAlert(AppMessages.NOT_PROCESSABLE).showAndWait();
			return;
		}
		
		engine.computeLists();
		// TODO enable export button 
	}
	
	@FXML
	public void exportLists() {
		if(engine == null) {
			FXMLUtils.warningAlert(AppMessages.NO_FILE_LOADED).showAndWait();
			return;
		}
		
		File dir = FXMLUtils.chooseDirectory(stage);
		// TODO
		
	}
	
	/* **************************
	 * 			CALLBACKS
	 * **************************/
	public SourceFile newSourceFile(File file) {
		return new SourceFile(file.getName(), AppOptions.instance.getOfferCount(), AppOptions.instance.getRoundBudget());
	}
	
	public void onReadSuccess(int itemAt) {
		table.getItems().get(itemAt).statusProperty().set(AppMessages.STATUS_OK);
	}
	
	public void onReadError(int itemAt, Throwable t) {
		table.getItems().get(itemAt).statusProperty().set(AppMessages.STATUS_ERR);
		if(t != null)
			table.getItems().get(itemAt).setError(t);
	}
	
	public void onValidationSuccess(int itemAt) {
		table.getItems().get(itemAt).statusProperty().set(AppMessages.STATUS_VALID);
	}
	
	public void onValidationError(int itemAt, Throwable t) {
		table.getItems().get(itemAt).statusProperty().set(AppMessages.STATUS_INVALID);
		if(t != null)
			table.getItems().get(itemAt).setError(t);
	}
	
	public void setTableDoubleClickAlert(String message) {
		FXMLUtils.setOnDoubleClick(table, event -> {
			SourceFile sf = table.getSelectionModel().getSelectedItem();
			if(sf.getError() == null)
				return;
			FXMLUtils.warningAlert(message, sf.getError()).showAndWait();
		});
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
		maxOffersField.textProperty().addListener((ob,ov,nv) -> AppOptions.instance.setOfferCount(Long.parseLong(nv.isEmpty() ? "0" : nv)));
		budgetField.textProperty().addListener((ob,ov,nv) -> AppOptions.instance.setRoundBudget(Double.parseDouble(nv.isEmpty() ? "0.0" : nv)));
		
		columnNamesField.setEditable(false);
	}
	
}
