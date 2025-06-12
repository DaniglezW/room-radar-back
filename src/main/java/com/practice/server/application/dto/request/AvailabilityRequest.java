package com.practice.server.application.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailabilityRequest {

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guests;

}
