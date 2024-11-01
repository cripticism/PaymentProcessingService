package org.example.payment.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.payment.exception.InvalidPaymentException;
import org.example.payment.exception.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle PaymentNotFoundException
    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("PAYMENT_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Handle InvalidPaymentException
    @ExceptionHandler(InvalidPaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidPayment(InvalidPaymentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_PAYMENT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Generic Exception handler for other exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ErrorResponse class to standardize error response structure
    @Setter
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private String errorCode;
        private String message;

    }
}
