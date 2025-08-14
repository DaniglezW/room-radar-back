package com.practice.server.application.dto;

import com.practice.server.application.model.entity.RoomImage;
import com.practice.server.application.model.enums.RoomType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RoomViewDto {

    private Long id;

    private RoomType type;

    private Double pricePerNight;

    private Integer maxGuests;

    private List<RoomImage> images;

    private byte[] mainImageData;

}