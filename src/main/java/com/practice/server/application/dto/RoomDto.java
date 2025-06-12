package com.practice.server.application.dto;

import com.practice.server.application.model.entity.RoomImage;
import com.practice.server.application.model.enums.RoomType;

import java.util.List;

public record RoomDto(
        Long id,
        String roomNumber,
        RoomType type,
        Double pricePerNight,
        Boolean available,
        Integer maxGuests,
        List<RoomImage> images
) {}
