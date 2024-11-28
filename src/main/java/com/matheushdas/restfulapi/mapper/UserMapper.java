package com.matheushdas.restfulapi.mapper;

import com.matheushdas.restfulapi.dto.auth.RegisterRequest;
import com.matheushdas.restfulapi.dto.auth.RegisterResponse;
import com.matheushdas.restfulapi.model.Permission;
import com.matheushdas.restfulapi.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public RegisterResponse toResponse(User data) {
        return new RegisterResponse(
                data.getUsername(),
                data.getFullName(),
                data.getEmail()
        );
    }

    public User toEntity(RegisterRequest data) {
        return new User(
                data.username(),
                data.fullName(),
                data.email(),
                data.password(),
                true,
                true,
                true,
                true,
                List.of(new Permission(3L, "COMMON_USER"))
        );
    }
}
