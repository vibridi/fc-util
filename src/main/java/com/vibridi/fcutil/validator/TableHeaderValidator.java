package com.vibridi.fcutil.validator;

import com.vibridi.fcutil.engine.XLSXReader;

public class TableHeaderValidator extends Validator<XLSXReader> {

	public TableHeaderValidator() {
	}

	@Override
	public boolean isValid(XLSXReader target) {
		return target.didFindTableHead();
	}

	@Override
	public String getMessage(XLSXReader target) {
		return "Errore di lettura del file " + target.getFileName() +
				": l'intestazione della tabella giocatori non e' stata trovata. " +
				"Verifica che tutti i nomi delle colonne siano identici a quelli specificati " +
				"e che non ci siano altri valori sulla stessa riga.";
	}

}
