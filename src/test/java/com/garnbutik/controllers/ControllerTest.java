package com.garnbutik.controllers;

import com.garnbutik.model.Credentials;
import com.garnbutik.model.User;
import com.garnbutik.security.JwtTokenIssuer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testConfig.WeldJUnit4Runner;

import javax.inject.Inject;
import java.text.MessageFormat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(WeldJUnit4Runner.class)
public class ControllerTest {

    @Inject
    JwtTokenIssuer tokenIssuer;

    private static final String TEST_USERNAME = "test";

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/timer-api/api/";
    }

    @Test
    public void whenLoginWithBadCredentials_then401(){
        given().contentType(ContentType.JSON).body(getBadCredentials()).log().all()
                .when().post("login")
                    .then().log().all().statusCode(401).body("statusCode", equalTo(401));
    }

    @Test
    public void whenLoginWithOKCredentials_then200(){
        given().contentType(ContentType.JSON).body(getOkCredentials()).log().all()
                .when().post("login")
                    .then().log().all().statusCode(200).body("token", notNullValue());
    }

    @Test
    public void whenRequestWithAuthToken_thenOK(){
        given()
                .log().all()
                .header(
                        "Authorization",
                        "Bearer " + getTokenForTestUser())
                .pathParam("username", TEST_USERNAME)
                    .when().get("users/{username}")
                        .then().statusCode(200);
    }

    @Test
    public void whenRequestWithoutAuthToken_then401(){
        given().log().all().pathParam("username", TEST_USERNAME)
                .when().get("users/{username}")
                    .then().statusCode(401);
    }

    private String getTokenForTestUser() {
        User user = new User();
        user.setUsername(ControllerTest.TEST_USERNAME);
        return tokenIssuer.issueToken(user);
    }

    private static Credentials getOkCredentials() {
        Credentials okCredentials = new Credentials();
        okCredentials.setPassword("testtest");
        okCredentials.setUsername("test");
        return okCredentials;
    }

    private static Credentials getBadCredentials() {
        Credentials badCredentials = new Credentials();
        badCredentials.setPassword("12345678");
        badCredentials.setUsername("notAUserName");
        return badCredentials;
    }
}
