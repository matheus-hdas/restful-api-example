package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class RegisterResponse {
    @JsonProperty("username")
    @NotBlank
    private String username;

    @JsonProperty("full_name")
    @NotBlank
    private String fullName;

    @JsonProperty("email")
    @NotBlank
    private String email;

    public RegisterResponse() {
    }

    public RegisterResponse(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegisterResponse that = (RegisterResponse) o;
        return Objects.equals(username, that.username) && Objects.equals(fullName, that.fullName) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullName, email);
    }
}