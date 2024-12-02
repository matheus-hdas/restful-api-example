package com.matheushdas.restfulapi.integration.controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheushdas.restfulapi.config.TestContextConfig;
import com.matheushdas.restfulapi.dto.auth.LoginRequest;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
import com.matheushdas.restfulapi.dto.person.UpdatePersonRequest;
import com.matheushdas.restfulapi.integration.container.ContainerEngine;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static com.matheushdas.restfulapi.util.MediaType.JSON;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonIntegrationTest extends ContainerEngine {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static CreatePersonRequest createPersonRequest;
    private static UpdatePersonRequest updatePersonRequest;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void authentication() {
        LoginRequest request = new LoginRequest("TestUsername", "testpassword");

        String response = given()
                .basePath("/auth/login")
                .port(TestContextConfig.SERVER_PORT)
                .contentType(JSON)
                .body(request)
                .when().post()
                .then().statusCode(200)
                .extract().body().as(LoginResponse.class).getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestContextConfig.AUTHORIZATION_HEADER_PARAM, "Bearer " + response)
                .setBasePath("/api/person")
                .setPort(TestContextConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(2)
    public void shouldReturnCreatedPersonResponse_whenCreatePerson() throws JsonProcessingException {
        mockCreateRequest();

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .body(createPersonRequest)
                .when().post()
                .then().statusCode(201)
                .extract().body().asString();

        PersonResponse createdPerson = objectMapper.readValue(body, PersonResponse.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getKey());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getKey() > 0);

        assertEquals("TestFirstName", createdPerson.getFirstName());
        assertEquals("TestLastName", createdPerson.getLastName());
        assertEquals("TestAddress", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void shouldReturnPersonResponseWithHisId_whenFindById() throws JsonProcessingException {

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().get("{id}")
                .then().statusCode(200)
                .extract().body().asString();

        PersonResponse foundedPerson = objectMapper.readValue(body, PersonResponse.class);

        assertNotNull(foundedPerson);
        assertNotNull(foundedPerson.getKey());
        assertNotNull(foundedPerson.getFirstName());
        assertNotNull(foundedPerson.getLastName());
        assertNotNull(foundedPerson.getAddress());
        assertNotNull(foundedPerson.getGender());

        assertEquals(1L, foundedPerson.getKey());
        assertEquals("TestFirstName", foundedPerson.getFirstName());
        assertEquals("TestLastName", foundedPerson.getLastName());
        assertEquals("TestAddress", foundedPerson.getAddress());
        assertEquals("Male", foundedPerson.getGender());
    }

    @Test
    @Order(4)
    public void shouldReturnUpdatedPersonResponse_whenUpdatePerson() throws JsonProcessingException {
        mockUpdateRequest();

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .body(updatePersonRequest)
                .when().put()
                .then().statusCode(200)
                .extract().body().asString();

        PersonResponse updatedPerson = objectMapper.readValue(body, PersonResponse.class);

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getKey());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());

        assertTrue(updatedPerson.getKey() > 0);

        assertEquals("UpdatedTestFirstName", updatedPerson.getFirstName());
        assertEquals("UpdatedTestLastName", updatedPerson.getLastName());
        assertEquals("UpdatedTestAddress", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
    }

    @Test
    @Order(5)
    public void shouldDeletePersonWithGivenIdRecord_whenDeletePerson() {
        given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().delete("{id}")
                .then().statusCode(204)
                .extract().body().asString();

        given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().get("{id}")
                .then().statusCode(404)
                .extract().body().asString();
    }

    @Test
    @Order(6)
    public void shouldReturnInvalidCorsRequest_whenAnyRequestWithInvalidOrigin() {
        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://invalidorigin.com")
                .contentType(JSON)
                .body(createPersonRequest)
                .when().post()
                .then().statusCode(403)
                .extract().body().asString();

        assertNotNull(body);
        assertEquals("Invalid CORS request", body);
    }

    private void mockCreateRequest() {
        createPersonRequest = new CreatePersonRequest(
               "TestFirstName",
               "TestLastName",
               "TestAddress",
               "Male"
        );
    }

    private void mockUpdateRequest() {
        updatePersonRequest = new UpdatePersonRequest(
                1L,
                "UpdatedTestFirstName",
                "UpdatedTestLastName",
                "UpdatedTestAddress",
                "Male"
        );
    }
}
