package com.vibridi.fcutil.concurrent;

import com.vibridi.fcutil.controller.MainViewController;
import com.vibridi.fcutil.engine.FCEngine;
import com.vibridi.fcutil.exception.ValidatorException;
import com.vibridi.fcutil.utils.AppMessages;

import javafx.concurrent.Task;

public class UITask extends Task<Void> {
	
	private MainViewController ctrl;
	private FCEngine engine;
	
	public UITask(MainViewController ctrl, FCEngine engine) {
		this.ctrl = ctrl;
		this.engine = engine;
	}

	@Override
	protected Void call() throws Exception {
		if(isCancelled())
			return null;

		try {
			engine.readOffers(ctrl::onReadSuccess, ctrl::onReadError);
			engine.validateOffers(ctrl::onValidationSuccess, ctrl::onValidationError);

		} catch(ValidatorException e) {
			ctrl.setTableDoubleClickAlert(e.getMessage());
			
		} catch(Throwable t) {
			ctrl.setTableDoubleClickAlert(AppMessages.FILE_LOAD_ERROR_BODY);
		}
		return null;
	}

}
