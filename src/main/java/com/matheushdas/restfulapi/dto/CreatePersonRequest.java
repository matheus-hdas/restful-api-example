package com.matheushdas.restfulapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePersonRequest(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("address") String address,
        @JsonProperty("gender") String gender
) {
}
