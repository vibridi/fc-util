package com.vibridi.fcutil.engine;

import java.util.List;

import com.vibridi.fcutil.model.Player;
import com.vibridi.fxmlutils.FXMLUtils;

public class XLSXWriter {

	private String owner;
	private List<Player> wonPlayers;
	
	public XLSXWriter(String owner, List<Player> wonPlayers) {
		this.owner = owner;
		this.wonPlayers = wonPlayers;
	}

	public String getOwner() {
		return owner;
	}

	public List<Player> getWonPlayers() {
		return wonPlayers;
	}
	
	public void prepareWorkbook() {
		// TODO write the workbook
	}
	
	public void saveWorkbook() {
		// TODO write file 
	}

}
