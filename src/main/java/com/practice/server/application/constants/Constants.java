package com.practice.server.application.constants;

public final class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String API_BASE = "/api/practice/v1";

    public static final String AUTH = "/auth";

    public static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public static final int ERROR_CODE = 2;

    public static final String ACCESS_TOKEN = "accessToken";

}
