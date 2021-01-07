package com.garnbutik.exceptions;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LoginExceptionMapper implements ExceptionMapper<LoginException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(LoginException e) {
        ApiError responseBody = new ApiError
                .Builder()
                .statusCode(401)
                .withTimeStamp()
                .errorMessage(e.getLocalizedMessage())
                .path(uriInfo.getAbsolutePath().toString())
                .build();

        return Response.status(401).entity(responseBody).build();
    }
}
