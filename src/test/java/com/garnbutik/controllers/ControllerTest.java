package com.garnbutik.controllers;
import com.garnbutik.model.User;
import com.garnbutik.security.JwtTokenIssuer;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testConfig.WeldJUnit4Runner;

import javax.inject.Inject;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

@RunWith(WeldJUnit4Runner.class)
public class ControllerTest {

    @Inject
    JwtTokenIssuer tokenIssuer;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/timer-api/api/";
    }


    @Test
    public void whenRequestWithAuthToken_thenOK(){
        given().log().all().header("Authorization", "Bearer " + getTokenForUser("garnbutik"))
                .when().request("GET", "users/garnbutik/")
                .then().statusCode(200);
    }

    @Test
    public void whenRequestWithoutAuthToken_thenOK(){
        given().log().all()
                .when().request("GET", "users/garnbutik/")
                .then().statusCode(401);
    }

    private String getTokenForUser(String username) {
        User user = new User();
        user.setUsername(username);
        return tokenIssuer.issueToken(user);
    }
}
