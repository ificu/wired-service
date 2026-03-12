package com.example.api.domain.exception;

public class WiredServiceNotFoundException extends RuntimeException {

    public WiredServiceNotFoundException(Long id) {
        super("WiredService not found: " + id);
    }
}
