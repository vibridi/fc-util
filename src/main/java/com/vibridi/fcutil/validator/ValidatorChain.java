package com.vibridi.fcutil.validator;

import com.vibridi.fcutil.exception.ValidatorException;

public class ValidatorChain<T> {
	
	private Validator<T> head;

	public void addValidator(Validator<T> validator) {
		if(head != null)
			head.append(validator);
		else
			head = validator;
	}
	
	public void validate(T object) throws ValidatorException {
		head.process(object);
	}
	
}
