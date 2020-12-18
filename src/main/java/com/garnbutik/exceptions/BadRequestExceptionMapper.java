package com.garnbutik.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(BadRequestException e) {
        CustomExceptionResponseBody responseBody = new CustomExceptionResponseBody
                .Builder()
                .statusCode(400)
                .errorMessage(e.getLocalizedMessage())
                .path(uriInfo.getAbsolutePath().toString())
                .withTimeStamp()
                .build();
        return Response.status(400).entity(responseBody).build();
    }
}
