package com.mavs.authservice.controller.handler;

import com.mavs.authservice.exception.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_REQUEST_RESOURCE_MESSAGE = "Request contains bad resources!";

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, BAD_REQUEST_RESOURCE_MESSAGE, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
