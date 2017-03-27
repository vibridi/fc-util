package com.vibridi.fcutil.controller;

import com.vibridi.fcutil.model.SourceFile;
import com.vibridi.fcutil.utils.AppMessages;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class StatusTableCell extends TableCell<SourceFile,String> {

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if(item == null || isEmpty()) {
			setText("");
			return;
		}
		switch(item) {
		case AppMessages.STATUS_ERR:
		case AppMessages.STATUS_INVALID:
			setTextFill(Color.DARKRED);
			break;
			
		default:
			setTextFill(Color.BLACK);
			break;
		}
		setText(item);
	}

}
