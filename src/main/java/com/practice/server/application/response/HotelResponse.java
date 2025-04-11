package com.practice.server.application.response;

import com.practice.server.domain.model.Hotel;
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
