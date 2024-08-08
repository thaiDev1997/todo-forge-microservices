package com.example.controller;

import com.example.dto.ValidationErrorResponse;
import com.example.exception.BaseResponseException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse constraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(ValidationErrorResponse.Violation.builder()
                    .fieldName(violation.getPropertyPath().toString())
                    .message(violation.getMessage())
                    .invalidValue(violation.getInvalidValue())
                    .build()
            );
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse methodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(ValidationErrorResponse.Violation.builder()
                    .fieldName(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .invalidValue(fieldError.getRejectedValue())
                    .build()
            );
        }
        return error;
    }
}
