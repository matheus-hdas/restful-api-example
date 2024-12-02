package com.matheushdas.restfulapi.service;

import com.matheushdas.restfulapi.dto.auth.LoginRequest;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.dto.auth.RegisterRequest;
import com.matheushdas.restfulapi.dto.auth.RegisterResponse;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.mapper.UserMapper;
import com.matheushdas.restfulapi.model.User;
import com.matheushdas.restfulapi.repository.UserRepository;
import com.matheushdas.restfulapi.security.jwt.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public LoginResponse login(LoginRequest login) {
        User user = userRepository.findByUsernameOrEmail(login.getUsernameOrEmail());

        if(user == null) throw new ResourceNotFoundException("No records found for this username or email!");

        String username = user.getUsername();
        String password = login.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return tokenProvider.generateAuthorizationToken(username, user.getRoles());
    }

    public LoginResponse refreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsernameOrEmail(username);

        if(user == null) throw new ResourceNotFoundException("No records found for this username or email!");

        return tokenProvider.refreshAccessToken(refreshToken);
    }

    public RegisterResponse register(RegisterRequest register) {
        User user = userMapper.toEntity(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        return userMapper.toResponse(userRepository.save(user));
    }
}
