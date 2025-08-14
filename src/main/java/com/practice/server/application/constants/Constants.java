package com.practice.server.application.constants;

public final class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String HOTEL_API_BASE = "/api/hotel/v1";
    public static final String SERVICES_API_BASE = "/api/service/v1";
    public static final String HOTEL_IMAGE_API_BASE = "/api/hotel-image/v1";
    public static final String ROOM_API_BASE = "/api/room/v1";
    public static final String ROOM_IMAGE_API_BASE = "/api/room-image/v1";
    public static final String RESERVATION_API_BASE = "/api/reservation/v1";
    public static final String REVIEW_API_BASE = "/api/review/v1";
    public static final String RESERVATION_HISTORY_API_BASE = "/api/reservation-history/v1";
    public static final String FAVORITES_API_BASE = "/api/favorites/v1";
    public static final String AUTH = "/api/auth";
    public static final String USER = "/api/user/api";


    public static final String SET_COOKIE = "Set-Cookie";

    public static final String GOOGLE = "google";

    public static final String LOCAL = "local";

    public static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public static final int OK = 0;

    public static final int ERROR_CODE = 2;

    public static final String ACCESS_TOKEN = "accessToken";

    public static final String USER_404 = "User not found";

    public static final int INVALID_GOOGLE_TOKEN = 101;

    public static final int GOOGLE_EMAIL_NOT_VERIFIED = 102;

    public static final int UNSUPPORTED_SOCIAL_PROVIDER = 103;

    public static final int GOOGLE_LOGIN_FAILED = 104;

    public static final int INVALID_CREDENTIALS = 105;

    public static final int PASSWORD_NOT_SET = 106;

    public static final int USER_NOT_FOUND = 107;

    public static final int EMAIL_ALREADY_REGISTERED_WITH_PASSWORD = 108;

    public static final int INVALID_PROVIDER = 109;

    public static final int ACCOUNT_EXISTS_NEEDS_CONFIRMATION = 110;

    public static final int ALREADY_LINKED = 111;

    public static final int EMAIL_MISMATCH = 112;

    public static final int HOTEL_OR_USER_NOT_FOUND = 113;

    public static final int FAVORITE_NOT_FOUND = 114;

}
