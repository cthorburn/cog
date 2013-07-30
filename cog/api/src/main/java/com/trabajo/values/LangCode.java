package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class LangCode extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public LangCode(String langCode) throws ValidationException {
		super(langCode);
	}

	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("[A-Z][A-Z]")) {
				//TODO IMPROVE
				throw new ValidationException("LangCode");
		}		
	}
}
