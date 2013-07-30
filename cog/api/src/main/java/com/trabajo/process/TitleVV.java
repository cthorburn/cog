package com.trabajo.process;

import java.io.Serializable;

import com.trabajo.ValidationException;


public class TitleVV extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public TitleVV(String title) throws ValidationException {
		super(title);
	}

	@Override
	protected void validate() throws ValidationException {
		if("".equals(getValue()))
			return;
		
		if(!getValue().matches("[A-Z][a-z]{2,32}")) {
				//TODO IMPROVE
				throw new ValidationException("title");
		}		
	}
}
