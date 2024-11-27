package com.matheushdas.restfulapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.exception.InvalidJwtAuthException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    @Value("${security.jwt.token.secret-key:secret}" )
    private String secretKey = "";

    @Value("${security.jwt.token.expire-length:360000}")
    private long validity = 3600000;

    private UserDetailsService userDetailsService;
    private Algorithm algorithm;

    public TokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public LoginResponse generateAuthorizationToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);
        String accessToken = getAccessToken(username, roles, now, expiration);
        String refreshToken = getRefreshToken(username, roles, now);

        return new LoginResponse(
                username,
                true,
                now,
                expiration,
                accessToken,
                refreshToken
        );
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date expiration) {
        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date expiration = new Date(now.getTime() + (validity * 3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decoded = decodeToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decoded.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken.startsWith("Bearer ")) return bearerToken.substring("Bearer ".length());
        return null;
    }

    public boolean validadeToken(String token) {
        DecodedJWT decoded = decodeToken(token);
        try {
            if(decoded.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthException("Expired or invalid token");
        }
    }
}
