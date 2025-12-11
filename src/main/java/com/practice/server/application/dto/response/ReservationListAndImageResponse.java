package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Reservation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationListAndImageResponse extends PracticeResponse  {

    private List<ReservationResponseDTO> reservation;

    public ReservationListAndImageResponse(int code, String message, LocalDateTime ts, List<ReservationResponseDTO> reservation) {
        super(code, message, ts);
        this.reservation = reservation;
    }

}
