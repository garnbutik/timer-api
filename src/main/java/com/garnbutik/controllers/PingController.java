package com.garnbutik.controllers;

import com.garnbutik.configuration.Secured;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("ping")
public class PingController {

    @Inject
    private Logger logger;

    @GET
    //@Secured
    public String get(){
        logger.info("I'm pinged!");
        return "Hello";
    }
    @GET
    @Secured
    @Path("{id}")
    public String get(@PathParam("id") Long id){
        return "Hello";
    }
}
