package com.vibridi.fcutil.concurrent;

import com.vibridi.fcutil.engine.FCEngine;

import javafx.concurrent.Task;

public class ComputeTask extends Task<Void> {

	private FCEngine engine;
	
	public ComputeTask(FCEngine engine) {
		this.engine = engine;
	}

	@Override
	protected Void call() throws Exception {
		if(isCancelled())
			return null;

		engine.computeLists();
		return null;
	}

}
