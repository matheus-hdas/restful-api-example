package com.matheushdas.restfulapi.dto.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdatePersonRequest(
        @JsonProperty("id") Long id,
        @JsonProperty("first_name") @NotBlank String firstName,
        @JsonProperty("last_name") @NotBlank String lastName,
        @JsonProperty("address") @NotBlank String address,
        @JsonProperty("gender") @NotBlank String gender
) {
}
