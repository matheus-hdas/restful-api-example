package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record LoginResponse(
        @JsonProperty("username") String username,
        @JsonProperty("authenticated") Boolean authenticated,
        @JsonProperty("created") Date created,
        @JsonProperty("expiration") Date expiration,
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}
