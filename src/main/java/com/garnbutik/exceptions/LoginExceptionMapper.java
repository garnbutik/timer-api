package com.garnbutik.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LoginExceptionMapper implements ExceptionMapper<LoginException> {

    @Override
    public Response toResponse(LoginException e) {
        return Response.status(401).entity(e.getLocalizedMessage()).build();
    }
}
