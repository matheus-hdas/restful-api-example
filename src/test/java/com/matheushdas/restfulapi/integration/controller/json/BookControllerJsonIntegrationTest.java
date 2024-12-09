package com.matheushdas.restfulapi.integration.controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.matheushdas.restfulapi.config.TestContextConfig;
import com.matheushdas.restfulapi.dto.auth.LoginRequest;
import com.matheushdas.restfulapi.dto.auth.LoginResponse;
import com.matheushdas.restfulapi.dto.book.CreateBookRequest;
import com.matheushdas.restfulapi.dto.book.BookResponse;
import com.matheushdas.restfulapi.dto.book.UpdateBookRequest;
import com.matheushdas.restfulapi.integration.container.ContainerEngine;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.List;

import static com.matheushdas.restfulapi.util.MediaType.JSON;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonIntegrationTest extends ContainerEngine {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static CreateBookRequest createBookRequest;
    private static UpdateBookRequest updateBookRequest;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(false));
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
                .setBasePath("/api/book")
                .setPort(TestContextConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(2)
    public void shouldReturnCreatedBookResponse_whenCreateBook() throws JsonProcessingException {
        mockCreateRequest();

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .body(createBookRequest)
                .when().post()
                .then().statusCode(201)
                .extract().body().asString();

        BookResponse createdBook = objectMapper.readValue(body, BookResponse.class);

        assertNotNull(createdBook);
        assertNotNull(createdBook.getKey());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());

        assertTrue(createdBook.getKey() > 0);

        assertEquals("TestTitle", createdBook.getTitle());
        assertEquals("TestAuthor", createdBook.getAuthor());
        assertEquals(49.99, createdBook.getPrice());
    }

    @Test
    @Order(3)
    public void shouldReturnBookResponseWithHisId_whenFindById() throws JsonProcessingException {

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().get("{id}")
                .then().statusCode(200)
                .extract().body().asString();

        BookResponse foundedBook = objectMapper.readValue(body, BookResponse.class);

        assertNotNull(foundedBook);
        assertNotNull(foundedBook.getKey());
        assertNotNull(foundedBook.getTitle());
        assertNotNull(foundedBook.getAuthor());
        assertNotNull(foundedBook.getLaunchDate());
        assertNotNull(foundedBook.getPrice());

        assertEquals(1L, foundedBook.getKey());
        assertEquals("TestTitle", foundedBook.getTitle());
        assertEquals("TestAuthor", foundedBook.getAuthor());
        assertEquals(49.99, foundedBook.getPrice());
    }

    @Test
    @Order(4)
    public void shouldReturnPageBookResponse_whenFindAll() throws JsonProcessingException {
        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .when().get()
                .then().statusCode(200)
                .extract().body().asString();

        PagedModel<EntityModel<BookResponse>> parsedBody = objectMapper.readValue(body, PagedModel.class);

        for (EntityModel<BookResponse> foundedBook : parsedBody) {
            assertNotNull(foundedBook.getContent());
            assertNotNull(foundedBook.getContent().getKey());
            assertNotNull(foundedBook.getContent().getTitle());
            assertNotNull(foundedBook.getContent().getAuthor());
            assertNotNull(foundedBook.getContent().getLaunchDate());
            assertNotNull(foundedBook.getContent().getPrice());

            assertEquals("TestTitle", foundedBook.getContent().getTitle());
            assertEquals("TestAuthor", foundedBook.getContent().getAuthor());
        }
    }

    @Test
    @Order(5)
    public void shouldReturnPageValidHateoasLinks_whenFindAll() throws JsonProcessingException {
        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .when().get()
                .then().statusCode(200)
                .extract().body().asString();

        System.out.println(body);

        assertTrue(body.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/1\"}}"));
        assertTrue(body.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book?page=0&size=12&direction=asc\"}}"));

        assertTrue(body.contains("\"page\":{\"size\":12,\"totalElements\":1,\"totalPages\":1,\"number\":0}}"));
    }

    @Test
    @Order(6)
    public void shouldReturnPageBookResponseWithNameLikeGiven_whenFindByName() throws JsonProcessingException {
        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("title", "testtitle")
                .when().get("/search/{title}")
                .then().statusCode(200)
                .extract().body().asString();

        PagedModel<EntityModel<BookResponse>> parsedBody = objectMapper.readValue(body, PagedModel.class);

        for (EntityModel<BookResponse> foundedBook : parsedBody) {
            assertNotNull(foundedBook.getContent());
            assertNotNull(foundedBook.getContent().getKey());
            assertNotNull(foundedBook.getContent().getTitle());
            assertNotNull(foundedBook.getContent().getAuthor());
            assertNotNull(foundedBook.getContent().getLaunchDate());
            assertNotNull(foundedBook.getContent().getPrice());

            assertEquals("TestTitle", foundedBook.getContent().getTitle());
            assertEquals("TestAuthor", foundedBook.getContent().getAuthor());
        }
    }

    @Test
    @Order(7)
    public void shouldReturnUpdatedBookResponse_whenUpdateBook() throws JsonProcessingException {
        mockUpdateRequest();

        String body = given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .body(updateBookRequest)
                .when().put()
                .then().statusCode(200)
                .extract().body().asString();

        BookResponse updatedBook = objectMapper.readValue(body, BookResponse.class);

        assertNotNull(updatedBook);
        assertNotNull(updatedBook.getKey());
        assertNotNull(updatedBook.getTitle());
        assertNotNull(updatedBook.getAuthor());
        assertNotNull(updatedBook.getLaunchDate());
        assertNotNull(updatedBook.getPrice());

        assertEquals(1L, updatedBook.getKey());
        assertEquals("UpdatedTestTitle", updatedBook.getTitle());
        assertEquals("UpdatedTestAuthor", updatedBook.getAuthor());
        assertEquals(59.99, updatedBook.getPrice());
    }

    @Test
    @Order(8)
    public void shouldDeleteBookWithGivenIdRecord_whenDeleteBook() {
        given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().delete("{id}")
                .then().statusCode(204);

        given().spec(specification)
                .header(TestContextConfig.ORIGIN_HEADER_PARAM, "http://localhost:3000")
                .contentType(JSON)
                .pathParam("id", 1L)
                .when().get("{id}")
                .then().statusCode(404);
    }

    private void mockCreateRequest() {
        createBookRequest = new CreateBookRequest(
                "TestTitle",
                "TestAuthor",
                new Date(),
                49.99
        );
    }

    private void mockUpdateRequest() {
        updateBookRequest = new UpdateBookRequest(
                1L,
                "UpdatedTestTitle",
                "UpdatedTestAuthor",
                new Date(),
                59.99
        );
    }
}
