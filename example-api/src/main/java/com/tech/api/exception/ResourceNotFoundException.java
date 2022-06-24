package com.tech.api.exception;

public class ResourceNotFoundException extends ValidationException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
