package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;

public class Category extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public Category(String desc) throws ValidationException {
		super(desc);
	}

	protected void validate() throws ValidationException {
		
	}

}
