package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.ReservationHistory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationHistoryResponse extends PracticeResponse {

    private ReservationHistory reservationHistory;

    public ReservationHistoryResponse(int code, String message, LocalDateTime ts, ReservationHistory reservationHistory) {
        super(code, message, ts);
        this.reservationHistory = reservationHistory;
    }


}
