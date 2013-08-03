package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;

public class Password extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public Password(String password) throws ValidationException {
		super(password);
	}

	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("([a-z]|_|[0-9]){2,32}")) {
				//TODO IMPROVE
				throw new ValidationException("Password");
		}		
	}
}
