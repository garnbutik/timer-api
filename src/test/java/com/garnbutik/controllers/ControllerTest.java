package com.garnbutik.controllers;
import com.garnbutik.model.User;
import com.garnbutik.security.JwtTokenIssuer;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testConfig.WeldJUnit4Runner;

import javax.inject.Inject;

import java.text.MessageFormat;

import static io.restassured.RestAssured.*;

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
    public void whenRequestWithAuthToken_thenOK(){
        String username = "test";
        given().log().all().header("Authorization", "Bearer " + getTokenForTestUser())
                .when().request("GET", MessageFormat.format("users/{0}/", TEST_USERNAME))
                .then().statusCode(200);
    }

    @Test
    public void whenRequestWithoutAuthToken_then401(){

        given().log().all()
                .when().request("GET", MessageFormat.format("users/{0}/", TEST_USERNAME))
                .then().statusCode(401);
    }

    private String getTokenForTestUser() {
        User user = new User();
        user.setUsername(ControllerTest.TEST_USERNAME);
        return tokenIssuer.issueToken(user);
    }
}
