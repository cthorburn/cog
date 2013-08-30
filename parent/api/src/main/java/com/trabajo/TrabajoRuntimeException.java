package com.trabajo;

public class TrabajoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TrabajoRuntimeException() {
		super();
	}

	public TrabajoRuntimeException(String message) {
		super(message);
	}

	public TrabajoRuntimeException(Throwable cause) {
		super(cause);
	}

	public TrabajoRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TrabajoRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
