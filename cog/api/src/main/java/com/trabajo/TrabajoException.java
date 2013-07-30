package com.trabajo;

public class TrabajoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TrabajoException() {
		super();
	}

	public TrabajoException(String message) {
		super(message);
	}

	public TrabajoException(Throwable cause) {
		super(cause);
	}

	public TrabajoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TrabajoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
