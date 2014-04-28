package com.cog.lang;

public class TypeConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public TypeConversionException(Class<?> toType, Object value) {
		super(String.format("Type conversion error. From: %s To: %s\n", value.getClass(), toType));
	}
}
