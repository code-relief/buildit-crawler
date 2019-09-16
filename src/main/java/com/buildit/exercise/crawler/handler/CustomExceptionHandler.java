package com.buildit.exercise.crawler.handler;

import com.buildit.exercise.crawler.exception.CrawlerException;
import com.buildit.exercise.crawler.model.BasicResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Log
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicResponse> handleInternalProblemsException(RuntimeException exception) {
        return buildResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({CrawlerException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<BasicResponse> handleBadRequestException(RuntimeException exception) {
        return buildResponse(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BasicResponse> buildResponse(final Exception exception, final HttpStatus httpStatus) {
        String message = exception.getMessage();
        String details = exception.getCause() != null ? exception.getCause().getMessage() : "";
        log.warning(message);
        return new ResponseEntity<>(new BasicResponse(message, details), httpStatus);
    }
}
