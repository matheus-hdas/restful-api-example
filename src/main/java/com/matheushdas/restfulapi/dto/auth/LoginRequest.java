package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @JsonProperty("username_or_email") @NotBlank String usernameOrEmail,
        @JsonProperty("password") @NotBlank String password
) {
}
