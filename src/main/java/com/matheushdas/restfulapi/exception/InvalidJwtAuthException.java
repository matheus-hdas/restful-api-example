package com.matheushdas.restfulapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthException extends RestAPIException {
    private final String message;

    public InvalidJwtAuthException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        pb.setTitle("Error with auth or refresh token");
        pb.setDetail(message);
        pb.setProperty("timestamp", new Date());

        return pb;
    }
}
