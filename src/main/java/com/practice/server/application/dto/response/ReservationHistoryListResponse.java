package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.ReservationHistory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationHistoryListResponse extends PracticeResponse {

    private List<ReservationHistory> reservationHistory;

    public ReservationHistoryListResponse(int code, String message, LocalDateTime ts, List<ReservationHistory> reservationHistory) {
        super(code, message, ts);
        this.reservationHistory = reservationHistory;
    }


}
