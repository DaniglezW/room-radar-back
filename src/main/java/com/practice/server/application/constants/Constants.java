package com.practice.server.application.constants;

public final class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String HOTEL_API_BASE = "/api/hotel/v1";

    public static final String AUTH = "/auth";



    public static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public static final int ERROR_CODE = 2;

    public static final String ACCESS_TOKEN = "accessToken";

}
