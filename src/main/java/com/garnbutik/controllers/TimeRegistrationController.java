package com.garnbutik.controllers;

import com.garnbutik.configuration.LocalDateConverter;
import com.garnbutik.configuration.Secured;
import com.garnbutik.mapper.CustomObjectMapper;
import com.garnbutik.model.dto.CreateTimeRegDTO;
import com.garnbutik.model.TimeRegistration;
import com.garnbutik.model.responseBodies.TimeRegistrationResponseBody;
import com.garnbutik.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("time")
public class TimeRegistrationController {

    @Inject
    private CustomObjectMapper customObjectMapper;

    @Inject
    private UserService userService;

    @Inject
    private LocalDateConverter dateConverter;

    @POST
    @Path("{username}/")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTimeRegistration(
            @PathParam("username") String username,
            @Valid CreateTimeRegDTO timeRegDTO,
            @Context UriInfo uriInfo) {
        TimeRegistration timeRegistration = customObjectMapper.createTimeRegDtoToTimeRegEntity(timeRegDTO);
        timeRegistration = userService.createTimeRegistration(timeRegistration);
        TimeRegistrationResponseBody responseBody = customObjectMapper.timeRegistrationToResponseBody(timeRegistration);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(Long.toString(timeRegistration.getId())).build();
        return Response.created(uri).entity(responseBody).build();
    }

    @GET
    @Path("{username}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeRegistrationsByUsername(
            @PathParam("username") String username,
            @DefaultValue("1970-01-01") @QueryParam("dateFrom") String queryDateFrom,
            @DefaultValue("2079-12-31") @QueryParam("dateTo") String queryDateTo) {
        LocalDate fromDate = dateConverter.fromString(queryDateFrom);
        LocalDate toDate = dateConverter.fromString(queryDateTo);
        List<TimeRegistrationResponseBody> responseBodyList =
                customObjectMapper.listOfTimeRegsToListOfTimeRegResponseBody(
                        userService.getTimeRegistrationsByUsernameWithDateFilter(username, fromDate, toDate));
        return Response.ok(responseBodyList).build();
    }

//    @PATCH
//    @Path("{username}/{timeRegID}")
//    @Secured
//    public Response updateTimeRegistration()



    @DELETE
    @Path("{username}/{timeRegID}")
    @Secured
    public Response deleteTimeRegistration(@PathParam("timeRegID") Long id) {
        userService.deleteTimeRegistration(id);
        return Response.noContent().build();
    }
}
