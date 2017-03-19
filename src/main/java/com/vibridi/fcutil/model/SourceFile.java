package com.vibridi.fcutil.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SourceFile {

	private StringProperty fileName;
	private LongProperty slots;
	private DoubleProperty budegt;
	private StringProperty status;
	private Throwable error;
	
	
	public SourceFile(String fileName, long slots, double budget) {
		this.fileName = new SimpleStringProperty(fileName);
		this.slots = new SimpleLongProperty(slots);
		this.budegt = new SimpleDoubleProperty(budget);
		this.status = new SimpleStringProperty();
		this.error = null;
	}


	public final StringProperty fileNameProperty() {
		return this.fileName;
	}
	


	public final String getFileName() {
		return this.fileNameProperty().get();
	}
	


	public final void setFileName(final String fileName) {
		this.fileNameProperty().set(fileName);
	}
	


	public final LongProperty slotsProperty() {
		return this.slots;
	}
	


	public final long getSlots() {
		return this.slotsProperty().get();
	}
	


	public final void setSlots(final long slots) {
		this.slotsProperty().set(slots);
	}
	


	public final DoubleProperty budegtProperty() {
		return this.budegt;
	}
	


	public final double getBudegt() {
		return this.budegtProperty().get();
	}
	


	public final void setBudegt(final double budegt) {
		this.budegtProperty().set(budegt);
	}
	


	public final StringProperty statusProperty() {
		return this.status;
	}
	


	public final String getStatus() {
		return this.statusProperty().get();
	}
	


	public final void setStatus(final String status) {
		this.statusProperty().set(status);
	}


	public Throwable getError() {
		return error;
	}


	public void setError(Throwable error) {
		this.error = error;
	}
	


	

}
