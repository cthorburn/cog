package com.trabajo.process;

import java.io.Serializable;

import com.trabajo.ValidationException;


public abstract class ValidatedValue<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T value;
	
	public ValidatedValue(T value) throws ValidationException {
		super();
		this.value = value;
		validate();
	}

	protected abstract void validate() throws ValidationException;

	public T getValue() {
		return value;
	} 
}
