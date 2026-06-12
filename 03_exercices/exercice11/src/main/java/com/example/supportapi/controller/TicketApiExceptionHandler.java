package com.example.supportapi.controller;

import com.example.supportapi.dto.ApiError;
import com.example.supportapi.exception.TicketNotFoundException;
import com.example.supportapi.exception.TicketStatusConflictException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketApiExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TicketNotFoundException exception) {
        throw new UnsupportedOperationException("Exception handler non implemente en phase RED");
    }

    @ExceptionHandler(TicketStatusConflictException.class)
    public ResponseEntity<ApiError> handleConflict(TicketStatusConflictException exception) {
        throw new UnsupportedOperationException("Exception handler non implemente en phase RED");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException exception) {
        throw new UnsupportedOperationException("Exception handler non implemente en phase RED");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        throw new UnsupportedOperationException("Exception handler non implemente en phase RED");
    }
}
