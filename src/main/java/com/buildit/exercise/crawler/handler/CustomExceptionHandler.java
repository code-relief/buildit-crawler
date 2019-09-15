package com.buildit.exercise.crawler.handler;

import com.buildit.exercise.crawler.model.BasicResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.MalformedURLException;

@ControllerAdvice
@Log
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicResponse> handleInternalProblemsException(RuntimeException exception) {
        return buildResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<BasicResponse> handleBadRequestException(MalformedURLException exception) {
        return buildResponse(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BasicResponse> buildResponse(final Exception exception, final HttpStatus httpStatus) {
        String message = exception.getMessage();
        String details = exception.getCause().getMessage();
        log.severe(message);
        return new ResponseEntity<>(new BasicResponse(message, details), httpStatus);
    }
}
