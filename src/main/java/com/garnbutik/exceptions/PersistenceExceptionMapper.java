package com.garnbutik.exceptions;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    @Inject
    Logger logger;

    @Override
    public Response toResponse(PersistenceException e) {
        logger.error(e.toString());
        return Response.status(409).entity(e.getCause().getCause().getLocalizedMessage()).build(); //TODO: fix error response
    }
}
