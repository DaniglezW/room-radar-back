package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Hotel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HotelResponse extends PracticeResponse {

    private Hotel hotel;

    public HotelResponse(int code, String message, LocalDateTime ts, Hotel hotel) {
        super(code, message, ts);
        this.hotel = hotel;
    }

}
