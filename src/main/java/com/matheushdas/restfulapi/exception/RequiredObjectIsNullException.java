package com.matheushdas.restfulapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RestAPIException {
    private final String message;

    public RequiredObjectIsNullException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Required object is null");
        pb.setDetail(message);
        pb.setProperty("timestamp", new Date());

        return pb;
    }
}
