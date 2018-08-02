package com.xptosystems.api;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String name, long id) {
        super(name + " with id " + id + " not found!");
    }

}
