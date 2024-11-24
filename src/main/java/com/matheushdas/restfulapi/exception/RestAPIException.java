package com.matheushdas.restfulapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class RestAPIException extends RuntimeException {
    private final String message;

    public RestAPIException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Internal Error");
        pb.setDetail(message);
        pb.setProperty("timestamp", new Date());

        return pb;
    }
}
