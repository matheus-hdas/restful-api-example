package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RegisterResponse(
        @JsonProperty("username") @NotBlank String username,
        @JsonProperty("full_name") @NotBlank String fullName,
        @JsonProperty("email") @NotBlank String email
) {
}
