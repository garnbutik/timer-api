package com.garnbutik.exceptions;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        ApiError apiErrorResponseBody = new ApiError
                .Builder()
                .errorMessage("Error processing request")
                .path(uriInfo.getAbsolutePath().toString())
                .statusCode(400)
                .withTimeStamp()
                .addViolationErrors(violations)
                .build();

        return Response.serverError().entity(apiErrorResponseBody).build();
    }
}
