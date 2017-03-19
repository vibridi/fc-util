package com.vibridi.fcutil.validator;

import java.util.List;
import java.util.stream.Collectors;

import com.vibridi.fcutil.engine.XLSXReader;

public class DuplicateNamesValidator extends Validator<XLSXReader> {

	public DuplicateNamesValidator() {
	}

	@Override
	public boolean isValid(XLSXReader target) {
		return !target.hasDuplicates();
	}

	@Override
	public String getMessage(XLSXReader target) {
		final int max = 3;

		List<String> names = target.getDuplicates();
		return new StringBuilder("Il file ")
				.append(target.getFileName())
				.append(" contiene ")
				.append(names.size())
				.append(" nomi di giocatori che compaiono più di una volta: ")
				.append(names.stream().limit(max).collect(Collectors.joining(", ")))
				.append(names.size() > max ? ("e altri " + (names.size() - max) + " giocatori.") : ".")
				.toString();
	}

}
