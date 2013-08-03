package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class FullName extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public FullName(String fullName) throws ValidationException {
		super(fullName);
	}

	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("(?i)[a-z \\.'-]{1,128}")) {
				//TODO IMPROVE
				throw new ValidationException("FullName");
		}		
	}
}
