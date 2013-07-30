package com.trabajo.values;


import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class EmailAddr extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EmailAddr(String email) throws ValidationException {
		super(email);
	}

	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("(?i)[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}")) {
				//TODO IMPROVE
				throw new ValidationException("EmailAddr");
		}		
	}
}

