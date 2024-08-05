package com.example.controller;

import com.example.exception.BaseResponseException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseResponseException.class)
    public ResponseEntity<String> baseExceptionResponse(BaseResponseException ex) {
        HttpStatus status = ex.getStatus();
        String message = ObjectUtils.defaultIfNull(ex.getMessage(), status.getReasonPhrase());
        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtimeException(RuntimeException ex) {
        ex.printStackTrace();
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }
}
