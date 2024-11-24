package com.matheushdas.restfulapi.integration.swagger;

import com.matheushdas.restfulapi.config.TestContextConfig;
import com.matheushdas.restfulapi.integration.container.ContainerEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends ContainerEngine {

    @Test
    public void shouldDisplaySwaggerUiPage() {
        String body = given()
                .basePath("/swagger-ui/index.html")
                .port(TestContextConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .extract()
                .body().asString();

        assertTrue(body.contains("Swagger UI"));
    }
}
