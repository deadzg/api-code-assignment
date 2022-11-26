package com.smalwe.api.exception;

import javax.naming.AuthenticationException;

public class UnProcessableEntityException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "400 Bad Request ";

    public UnProcessableEntityException() {
        super(DEFAULT_MESSAGE);
    }

    public UnProcessableEntityException(String exception) {
        super(exception);
    }
}
