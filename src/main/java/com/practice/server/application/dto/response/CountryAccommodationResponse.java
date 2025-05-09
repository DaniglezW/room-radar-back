package com.practice.server.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CountryAccommodationResponse extends PracticeResponse {

    private String country;
    private Long accommodations;

    public CountryAccommodationResponse(int code, String message, LocalDateTime ts, String country, Long accommodations) {
        super(code, message, ts);
        this.country = country;
        this.accommodations = accommodations;
    }

}
