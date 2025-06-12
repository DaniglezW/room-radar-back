package com.practice.server.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableRoomResponse {

    private Long roomId;
    private String roomNumber;
    private String type;
    private Double pricePerNight;
    private Boolean available;
    private Integer maxGuests;

}

