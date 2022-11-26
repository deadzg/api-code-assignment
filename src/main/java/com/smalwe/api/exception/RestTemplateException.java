package com.smalwe.api.exception;

public class RestTemplateException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Error fetching data";

    public RestTemplateException() {
        super(DEFAULT_MESSAGE);
    }

    public RestTemplateException(String exception) {
        super(exception);
    }
}
