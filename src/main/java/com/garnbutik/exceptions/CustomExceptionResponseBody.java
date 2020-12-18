package com.garnbutik.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CustomExceptionResponseBody {

    private int statusCode;
    private String errorMessage;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private CustomExceptionResponseBody(){

    }

    public static class Builder {
        private int statusCode;
        private String errorMessage;
        private String path;
        private LocalDateTime time;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder withTimeStamp() {
            this.time = LocalDateTime.now();
            return this;
        }

        public CustomExceptionResponseBody build() {
            CustomExceptionResponseBody responseBody = new CustomExceptionResponseBody();
            responseBody.errorMessage = this.errorMessage;
            responseBody.statusCode = statusCode;
            responseBody.path = this.path;
            responseBody.time = LocalDateTime.now();
            return responseBody;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
