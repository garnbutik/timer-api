package com.garnbutik.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Named
@ApplicationScoped
public class JacksonObjectMapperProducer {

    @Produces
    public ObjectMapper getObjectMapperWithJSR353Module() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR353Module());
        return objectMapper;
    }
}
