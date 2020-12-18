package com.garnbutik.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException e) {
        CustomExceptionResponseBody responseBody =
                new CustomExceptionResponseBody.Builder()
                    .path(uriInfo.getAbsolutePath().toString())
                    .errorMessage(e.getLocalizedMessage())
                    .statusCode(404)
                    .withTimeStamp()
                    .build();

        return Response.status(responseBody.getStatusCode()).entity(responseBody).build();
    }
}
