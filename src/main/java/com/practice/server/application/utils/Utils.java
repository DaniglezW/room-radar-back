package com.practice.server.application.utils;

import com.practice.server.application.model.entity.RoomImage;

import java.security.SecureRandom;
import java.util.List;

public class Utils {
    private Utils() {}

    public static String generateSecureRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    public static byte[] getMainRoomImageData(List<RoomImage> images) {
        if (images == null || images.isEmpty()) return null;

        return images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                .findFirst()
                .or(() -> images.stream().findFirst())
                .map(RoomImage::getImageData)
                .orElse(null);
    }

}
