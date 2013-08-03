package com.trabajo.vcl;

import java.io.Serializable;

public class ResourceUnavailableException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ResourceUnavailableException() {
        super();
    }

    public ResourceUnavailableException(String message) {
        super(message);
    }

    public ResourceUnavailableException(Throwable cause) {
        super(cause);
    }

    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
