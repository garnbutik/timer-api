package com.garnbutik.configuration;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class Configuration {

    @Inject
    private Logger logger;
    private Properties properties;

    @PostConstruct
    public void initialize(){
        properties = new Properties();
        InputStream inputStream = Configuration.class.getResourceAsStream("/application.properties");
        if (inputStream == null) {
            throw new RuntimeException("Cannot load properties");
        }
        try {
            this.properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Could not load properties!");
            throw new RuntimeException("Cannot load properties");
        }
    }

    public String getStringProperty(String key) {
        return this.properties.getProperty(key);
    }

    public Long getLongProperty(String key) {
        try {
            return Long.parseLong(this.properties.getProperty(key));
        } catch (NumberFormatException exception) {
            throw new RuntimeException("Could not parse property: " + key);
        }

    }
}
