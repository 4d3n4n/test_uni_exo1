package com.example.reservationapi.controller;

import com.example.reservationapi.dto.ApiError;
import com.example.reservationapi.exception.ReservationConflictException;
import com.example.reservationapi.exception.ReservationNotFoundException;
import com.example.reservationapi.exception.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les exceptions métier et de validation en réponses HTTP (404 / 409 / 400)
 * au format {@link ApiError}.
 */
@RestControllerAdvice
public class ReservationApiExceptionHandler {

    @ExceptionHandler({RoomNotFoundException.class, ReservationNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, exception.getMessage()));
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ReservationConflictException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity
                .badRequest()
                .body(ApiError.of(400, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Requête invalide");

        return ResponseEntity
                .badRequest()
                .body(ApiError.of(400, message));
    }
}
