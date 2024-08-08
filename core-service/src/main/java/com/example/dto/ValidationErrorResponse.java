package com.example.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    @Builder
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    public static class Violation {
        String fieldName;
        String message;
        Object invalidValue;
    }
}
