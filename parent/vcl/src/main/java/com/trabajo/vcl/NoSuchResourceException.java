package com.trabajo.vcl;

import java.io.Serializable;

public class NoSuchResourceException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public NoSuchResourceException(String message) {
		super(message);
	}
	/*

	public NoSuchResourceException(Throwable cause) {
		super(cause);
	}

	public NoSuchResourceException(String message, Throwable cause) {
		super(message, cause);
	}
*/
}
