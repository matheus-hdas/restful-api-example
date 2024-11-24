package com.matheushdas.restfulapi.exception.handler;

import com.matheushdas.restfulapi.exception.RequiredObjectIsNullException;
import com.matheushdas.restfulapi.exception.ResourceNotFoundException;
import com.matheushdas.restfulapi.exception.RestAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ProblemDetail handleAllExceptions(Exception ex) {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        pb.setTitle("Internal Error");
        pb.setDetail("Error into internal Server");
        pb.setProperty("timestamp", new Date());

        return pb;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle("Argument is not valid");
        pb.setDetail("Any fields are not valid");
        pb.setProperty("timestamp", new Date());

        return pb;
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ProblemDetail handleRequiredObjectIsNullException(RequiredObjectIsNullException ex) {
        return ex.toProblemDetail();
    }
}
