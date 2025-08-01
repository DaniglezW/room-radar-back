package com.practice.server.application.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtils {

    private static final ResourceBundle messages = ResourceBundle.getBundle("error", Locale.getDefault());

    private MessageUtils() {}

    public static String getMessage(int code, Object... args) {
        String message = messages.getString(String.valueOf(code));
        return MessageFormat.format(message, args);
    }

}
