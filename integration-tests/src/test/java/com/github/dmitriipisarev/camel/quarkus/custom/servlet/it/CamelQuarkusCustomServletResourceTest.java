package com.github.dmitriipisarev.camel.quarkus.custom.servlet.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CamelQuarkusCustomServletResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/camel-quarkus-custom-servlet")
                .then()
                .statusCode(200)
                .body(is("Hello camel-quarkus-custom-servlet"));
    }
}
