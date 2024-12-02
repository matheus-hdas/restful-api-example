package com.matheushdas.restfulapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class RegisterRequest {
    @JsonProperty("username")
    @NotBlank
    private String username;

    @JsonProperty("full_name")
    @NotBlank
    private String fullName;

    @JsonProperty("email")
    @NotBlank
    private String email;

    @JsonProperty("password")
    @NotBlank
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest request = (RegisterRequest) o;
        return Objects.equals(username, request.username) && Objects.equals(fullName, request.fullName) && Objects.equals(email, request.email) && Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullName, email, password);
    }
}