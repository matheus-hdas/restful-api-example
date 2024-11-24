package com.matheushdas.restfulapi.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

public record CreateBookRequest(
        @JsonProperty("title") @NotBlank String title,
        @JsonProperty("author") @NotBlank String author,
        @JsonProperty("launch_date") @NotBlank Date launchDate,
        @JsonProperty("price") @NotBlank Double price
) {
}
