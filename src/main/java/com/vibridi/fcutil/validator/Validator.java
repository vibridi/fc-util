package com.vibridi.fcutil.validator;

import com.vibridi.fcutil.exception.ValidatorException;

public abstract class Validator<T> {

	private Validator<T> next;
	
	public void append(Validator<T> next) {
		if(this.next != null)
			this.next.append(next);
		else
			this.next = next;
	}
	
	public Validator<T> next() {
		return next;
	}
	
	public void process(T target) throws ValidatorException {
		if(!isValid(target))
			throw new ValidatorException(getMessage(target));
		
		if(next != null)
			next.process(target);
	}
	
	public abstract boolean isValid(T target);
	public abstract String getMessage(T target);
}
