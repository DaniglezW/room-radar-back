package com.practice.server.application.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationRequest {

    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guests;

    private List<String> guestNames;
    private String guestEmail;
    private String guestPhone;
    private String cardNumber;
    private String paymentMethod;

}
