package com.matheushdas.restfulapi.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;


public record CreateBookRequest(
        @JsonProperty("title") @NotBlank String title,
        @JsonProperty("author") @NotBlank String author,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonProperty("launch_date") Date launchDate,
        @JsonProperty("price") Double price
) {
}
