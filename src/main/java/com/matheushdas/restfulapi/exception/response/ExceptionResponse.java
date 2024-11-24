package com.matheushdas.restfulapi.exception.response;

import org.springframework.web.context.request.WebRequest;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {
    private final Date timestamp;
    private final String status;
    private final String message;
    private final String details;

    public static ExceptionResponse of(String message, String status, WebRequest request) {
        return new ExceptionResponse(
                new Date(),
                message,
                status,
                request.getDescription(false)
        );
    }

    private ExceptionResponse(Date timestamp, String status, String message, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
