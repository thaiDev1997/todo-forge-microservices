package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ResourceDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank
    String title;
    @NotBlank
    String code;
    String description;
    @JsonProperty("isActive")
    boolean isActive;

    public ResourceDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String code,
                       String description, boolean isActive) {
        super(id, createdAt, updatedAt);
        this.title = title;
        this.code = code;
        this.description = description;
        this.isActive = isActive;
    }

    public ResourceDTO(String title, String code, String description, boolean isActive) {
        this.title = title;
        this.code = code;
        this.description = description;
        this.isActive = isActive;
    }
}
