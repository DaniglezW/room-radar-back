package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Reservation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationResponse extends PracticeResponse {

    private Reservation reservation;

    public ReservationResponse(int code, String message, LocalDateTime ts, Reservation reservation) {
        super(code, message, ts);
        this.reservation = reservation;
    }

}
