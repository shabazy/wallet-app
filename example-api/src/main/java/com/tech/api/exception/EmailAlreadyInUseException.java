package com.tech.api.exception;

public class EmailAlreadyInUseException extends ValidationException {

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
