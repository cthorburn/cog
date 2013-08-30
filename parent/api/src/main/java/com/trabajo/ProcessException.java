package com.trabajo;

public class ProcessException extends TrabajoRuntimeException {

	private static final long serialVersionUID = 1L;

	public ProcessException() {
		super();
	}

	public ProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(Throwable cause) {
		super(cause);
	}


}
