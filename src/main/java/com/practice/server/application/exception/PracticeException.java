package com.practice.server.application.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PracticeException extends RuntimeException {
    private final int code;
    private final String message;
    private final LocalDateTime ts;

    public PracticeException(int code, String body) {
        super(body);
        this.code = code;
        this.message = body;
        this.ts = LocalDateTime.now();
    }

}
