package com.trabajo.values;

import java.io.Serializable;

import com.trabajo.ValidationException;
import com.trabajo.process.ValidatedValue;


public class Description extends ValidatedValue<String> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int length;
	
	public Description(String desc, int length) throws ValidationException {
		super(desc);
		this.length=length;
		validate1();
	}
	
	@Override
	protected void validate() throws ValidationException {
	}
	
	private void validate1() throws ValidationException {
		if(!getValue().matches(".{1,"+length+"}")) {
			//TODO IMPROVE
			throw new ValidationException("Description");
		}	
	}		
	
}
