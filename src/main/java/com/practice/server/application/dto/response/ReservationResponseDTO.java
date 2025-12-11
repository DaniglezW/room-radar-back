package com.practice.server.application.dto.response;

import com.practice.server.application.model.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationResponseDTO {

    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Integer guests;
    private Double totalPrice;

    private String guestNames;
    private String guestEmail;
    private String guestPhone;

    private String paymentMethod;
    private String cardNumber;
    private String confirmationCode;

    private ReservationStatus status;
    private LocalDateTime createdAt;

    private Long hotelId;
    private String hotelName;

    private byte[] mainImage;

}
