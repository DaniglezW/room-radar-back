package com.practice.server.application.dto;

import com.practice.server.application.model.entity.HotelImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class HotelWithRoomsDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
    private Integer stars;
    private List<HotelImage> images;
    private List<RoomViewDto> availableRooms;

}
