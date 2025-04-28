package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Hotel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HotelListResponse extends PracticeResponse {

    private List<Hotel> hotels;

    public HotelListResponse(int code, String message, LocalDateTime ts, List<Hotel> hotels) {
        super(code, message, ts);
        this.hotels = hotels;
    }
}
