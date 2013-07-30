package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class RoleName extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	public RoleName(String roleName) throws ValidationException {
		super(roleName);
	}
	
	@Override
	protected void validate() throws ValidationException {
		if(!getValue().matches("([a-z_0-9]){2,64}")) {
				//TODO IMPROVE
				throw new ValidationException("RoleName");
		}		
	}
}
