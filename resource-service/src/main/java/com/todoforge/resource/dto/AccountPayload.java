package com.todoforge.resource.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String PAYLOAD_NAME = "account";

    String firstName;
    String lastName;
    String emailAddress;
}
