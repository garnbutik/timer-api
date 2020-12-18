package com.garnbutik.configuration;

import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.time.DateTimeException;
import java.time.LocalDate;



@RequestScoped
public class LocalDateConverter {

    public LocalDate fromString(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeException exception) {
            throw new BadRequestException("Bad date format: " + dateString);
        }
    }

    public String toString(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.toString();
    }
}
