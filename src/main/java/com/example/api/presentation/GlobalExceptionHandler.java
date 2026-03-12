package com.example.api.presentation;

import com.example.api.domain.exception.WiredServiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WiredServiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleWiredServiceNotFound(WiredServiceNotFoundException e) {
    }
}
