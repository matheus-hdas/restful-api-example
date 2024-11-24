package com.matheushdas.restfulapi.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheushdas.restfulapi.config.TestContextConfig;
import com.matheushdas.restfulapi.dto.person.CreatePersonRequest;
import com.matheushdas.restfulapi.dto.person.PersonResponse;
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

    private static PersonResponse personResponse;
    private static CreatePersonRequest createPersonRequest;

    @BeforeAll
    public static void setUp() {
        personResponse = new PersonResponse();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void shouldReturnCreatedPersonResponse_whenCreatePerson() throws JsonProcessingException {
        mockCreateRequest();

        specification = new RequestSpecBuilder()
                .addHeader(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .setBasePath("/api/person")
                .setPort(TestContextConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String body = given().spec(specification)
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
    @Order(2)
    public void shouldReturnInvalidCorsRequest_whenCreatePersonWithInvalidOrigin() {
        specification = new RequestSpecBuilder()
                .addHeader(TestContextConfig.ORIGIN_HEADER_PARAM, "http://invalidorigin.com")
                .setBasePath("/api/person")
                .setPort(TestContextConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String body = given().spec(specification)
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
}
