package com.example.demo.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UsersExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { RuntimeException.class })
    protected void handleConflict(RuntimeException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleGeneralError(Exception ex, WebRequest request) {
//        ApiError apiError = new ApiError("Какое-то описание ошибки");
        return new ResponseEntity<>( new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Обрабатывает исключения специального типа, например MyException
//    @ExceptionHandler(value = { MyException.class })
//    protected ResponseEntity<Object> handleMyException(MyException ex, WebRequest request) {
//        ApiError apiError = new ApiError("Какое-то описание ошибки");
//        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//    }

}