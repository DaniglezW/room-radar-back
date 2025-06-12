package com.practice.server.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(PracticeException.class)
    public ResponseEntity<Object> handlePracticeException(PracticeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", ex.getCode());
        response.put("message", ex.getMessage());
        response.put("ts", ex.getTs());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
