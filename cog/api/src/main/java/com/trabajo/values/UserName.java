package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class UserName extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public UserName(String userName) throws ValidationException {
		super(userName);
	}


	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("([a-z_0-9]){2,32}")) {
				throw new ValidationException("UserName");
		}		
	}
}
