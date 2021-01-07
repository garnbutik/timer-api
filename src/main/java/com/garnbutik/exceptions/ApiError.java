package com.garnbutik.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApiError {

    private int statusCode;
    private String errorMessage;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;
    private List<ApiSubError> subErrors;

    private ApiError(){

    }

    public static class Builder {
        private int statusCode;
        private String errorMessage;
        private String path;
        private LocalDateTime time;
        private List<ApiSubError> subErrors;

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
        public Builder addSubError(ApiSubError subError) {
            if (subErrors == null) {
                subErrors = new ArrayList<>();
            }
            subErrors.add(subError);
            return this;
        }
        public Builder addViolationErrors(Set<ConstraintViolation<?>> violationErrors) {
            if (subErrors == null) {
                subErrors = new ArrayList<>();
            }
            for (ConstraintViolation<?> constraintViolation : violationErrors){
                ApiSubError subError = new ApiSubError();
                if (constraintViolation.getInvalidValue() != null) {
                    subError.setRejectedValue(constraintViolation.getInvalidValue().toString());
                }
                subError.setField(constraintViolation.getPropertyPath().toString());
                subError.setMessage(constraintViolation.getMessage());
                subErrors.add(subError);
            }
            return this;
        }

        public ApiError build() {
            ApiError responseBody = new ApiError();
            responseBody.errorMessage = this.errorMessage;
            responseBody.statusCode = statusCode;
            responseBody.path = this.path;
            responseBody.time = LocalDateTime.now();
            responseBody.subErrors = this.subErrors;
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

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }
}
