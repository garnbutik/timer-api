package com.garnbutik.configuration;

import com.garnbutik.exceptions.LoginException;
import com.garnbutik.model.User;
import com.garnbutik.security.JwtTokenParser;
import com.garnbutik.security.TokenParser;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private TokenParser tokenParser;

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext context) {
        String header = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            logger.info("No Authorization header provided");
            return;
        }

        String token = header.substring("Bearer".length()).trim();
        User user = tokenParser.parseToken(token);
        MultivaluedMap<String, String> pathParams = context.getUriInfo().getPathParameters();
        long id = -1L;
        try {
            id = Long.parseLong(pathParams.getFirst("id"));

        } catch (NumberFormatException numberFormatException) {
            logger.info("No id in path");
        }
        String username = pathParams.getFirst("username");

        if (user.getId().equals(id) || user.getUsername().equals(username)) {
            return;
        }
        logger.warn(String.format("Unauthorized attempt to access time registrations by %s", user.getUsername()));
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        return;
    }
}
