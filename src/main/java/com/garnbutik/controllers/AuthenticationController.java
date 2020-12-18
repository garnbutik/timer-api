package com.garnbutik.controllers;

import com.garnbutik.configuration.Configuration;
import com.garnbutik.model.Credentials;
import com.garnbutik.model.responseBodies.LoginResponseBody;
import com.garnbutik.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("login")
public class AuthenticationController {

    @Inject
    private UserService userService;

    @Inject
    private Configuration configuration;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid Credentials credentials) throws Exception {
        String token = userService.authenticate(credentials.getUsername(), credentials.getPassword());
        LoginResponseBody responseBody = new LoginResponseBody();
        responseBody.setToken(token);
        responseBody.setTokenType("JWT");
        responseBody.setUsername(credentials.getUsername());
        responseBody.setExpiresIn(configuration.getStringProperty("authentication.jwt.defaultExpirationTime"));
        return Response.ok("ok").entity(responseBody).build();
    }
}
