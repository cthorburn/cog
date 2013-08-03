package com.trabajo.engine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.trabajo.ValidationException;
import com.trabajo.annotation.Activation;

public class ActivationSpec {

	private static final ActivationSpec DEFAULT;
	
	static {
		DEFAULT=new ActivationSpec();
		DEFAULT.at=null;
		DEFAULT.deprecatePreviousVersions=false;
	}
	
	
	private GregorianCalendar at;
	private boolean deprecatePreviousVersions;
	
	public static ActivationSpec forClass(Class<?> clazz) throws ValidationException {
		Activation a=clazz.getAnnotation(Activation.class);
		
		if(a!=null) {
			ActivationSpec spec=new ActivationSpec();
			spec.deprecatePreviousVersions=a.deprecatePreviousVersions();
			spec.at=new GregorianCalendar();
			try {
				spec.at.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(a.at()));
			} catch (ParseException e) {
				throw new ValidationException("Activation at format: "+a.at());
			}
		}
		return ActivationSpec.DEFAULT;
	}

	public GregorianCalendar getAt() {
		return at;
	}

	public boolean isDeprecatePreviousVersions() {
		return deprecatePreviousVersions;
	}
}
