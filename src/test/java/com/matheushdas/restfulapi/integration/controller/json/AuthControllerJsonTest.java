package com.matheushdas.restfulapi.integration.controller.json;

import com.matheushdas.restfulapi.config.TestContextConfig;
import com.matheushdas.restfulapi.dto.auth.LoginRequest;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.dto.auth.RegisterRequest;
import com.matheushdas.restfulapi.dto.auth.RegisterResponse;
import com.matheushdas.restfulapi.integration.container.ContainerEngine;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static com.matheushdas.restfulapi.util.MediaType.JSON;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends ContainerEngine {
    private static LoginResponse loginToken;

    @Test
    @Order(1)
    public void shouldReturnCreatedUserResponse_whenRegister() {
        RegisterRequest request = new RegisterRequest(
                "AuthTestUsernameJson",
                "AuthTestFullNameJson",
                "authtestjson@test.com",
                "AuthTestPasswordJson"
        );

        RegisterResponse response = given()
                .basePath("/auth/register")
                .port(TestContextConfig.SERVER_PORT)
                .contentType(JSON)
                .body(request)
                .when().post()
                .then().statusCode(200)
                .extract().body().as(RegisterResponse.class);

        assertNotNull(response);

        assertEquals("AuthTestUsernameJson", response.getUsername());
        assertEquals("AuthTestFullNameJson", response.getFullName());
        assertEquals("authtestjson@test.com", response.getEmail());
    }

    @Test
    @Order(2)
    public void shouldAuthenticateUserAndReturnAuthToken_whenLogin() {
        LoginRequest request = new LoginRequest("AuthTestUsernameJson", "AuthTestPasswordJson");

        loginToken = given()
                .basePath("/auth/login")
                .port(TestContextConfig.SERVER_PORT)
                .contentType(JSON)
                .body(request)
                .when().post()
                .then().statusCode(200)
                .extract().body().as(LoginResponse.class);

        assertNotNull(loginToken.getAccessToken());
        assertNotNull(loginToken.getRefreshToken());
    }

    @Test
    @Order(3)
    public void shouldRefreshAccessToken_whenRefreshToken() {
        LoginResponse refreshedToken = given()
                .basePath("/auth/refresh")
                .port(TestContextConfig.SERVER_PORT)
                .contentType(JSON)
                .pathParam("username", loginToken.getUsername())
                .header(TestContextConfig.AUTHORIZATION_HEADER_PARAM, "Bearer " + loginToken.getRefreshToken())
                .when().put("{username}")
                .then().statusCode(200)
                .extract().body().as(LoginResponse.class);

        assertNotNull(refreshedToken.getAccessToken());
        assertNotNull(refreshedToken.getRefreshToken());

        assertTrue(refreshedToken.getAccessToken() != loginToken.getAccessToken());
        assertTrue(refreshedToken.getRefreshToken() != loginToken.getRefreshToken());
    }
}
