package com.example.reservationapi.controller;

import com.example.reservationapi.dto.ApiError;
import com.example.reservationapi.exception.ReservationConflictException;
import com.example.reservationapi.exception.ReservationNotFoundException;
import com.example.reservationapi.exception.RoomNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationApiExceptionHandler {

    @ExceptionHandler({RoomNotFoundException.class, ReservationNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException exception) {
        throw new UnsupportedOperationException("Exception handler non implemente en phase RED");
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ReservationConflictException exception) {
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
