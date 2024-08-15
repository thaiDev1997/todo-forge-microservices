package com.example.controller;

import com.example.exception.BaseResponseException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String message = null;
        try (InputStream inputStream = response.body().asInputStream()) {
            message = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            log.error(ObjectUtils.defaultIfNull(message, e.getMessage()), e);
            if (Objects.isNull(message)) message = "Feign Proxy Exception";
        }
        log.error(message);
        return new BaseResponseException(message, HttpStatus.resolve(response.status()));
    }
}
