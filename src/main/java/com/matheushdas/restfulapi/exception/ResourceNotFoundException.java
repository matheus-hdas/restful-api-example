package com.matheushdas.restfulapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RestAPIException {
    private final String message;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Resource not found");
        pb.setDetail(message);
        pb.setProperty("timestamp", new Date());

        return pb;
    }
}
