package com.practice.server.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReviewResponse extends PracticeResponse {

    private List<ShowReview> reviews;

    public ReviewResponse(int code, String message, LocalDateTime ts, List<ShowReview> reviews) {
        super(code, message, ts);
        this.reviews = reviews;
    }

}
