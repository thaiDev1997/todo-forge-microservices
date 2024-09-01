package com.todoforge.core.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BaseResponseException extends RuntimeException {

    HttpStatus status;

    public BaseResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BaseResponseException(HttpStatus status) {
        super(status.getReasonPhrase());
        this.status = status;
    }
}
