package com.garnbutik.controllers;

import com.garnbutik.configuration.Secured;
import com.garnbutik.mapper.CustomObjectMapper;
import com.garnbutik.model.dto.CreateUserDTO;
import com.garnbutik.model.dto.UserResponseBody;
import com.garnbutik.service.UserService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Path("users/{username}")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    CustomObjectMapper customObjectMapper;

    @Inject
    Logger logger;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid CreateUserDTO createUserDTO, @Context UriInfo uriInfo) {
        UserResponseBody userResponseBody = customObjectMapper.userToResponseUserDto(userService.createUser(createUserDTO));
        int userId = userResponseBody.getId().intValue();
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(Integer.toString(userId)).build();
        logger.info(String.format("User with username %s and id %s is created",
                userResponseBody.getUsername(), userResponseBody.getId()));
        return Response.created(uri).entity(userResponseBody).build();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        UserResponseBody userResponseBody = customObjectMapper.userToResponseUserDto(userService.getUserByUsernameOrEmail(username));
        return Response.ok().entity(userResponseBody).build();
    }

    @PATCH
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchUser(@PathParam("username") String id, String jsonPatch, @Context UriInfo uriInfo) {
        if (jsonPatch == null || jsonPatch.isEmpty()) {
            throw new BadRequestException("Please provide patch request");
        }
        userService.updateUser(jsonPatch, uriInfo);
        return Response.ok().build();
    }
}
