package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty("username_or_email") String usernameOrEmail,
        @JsonProperty("password") String password
) {
}
