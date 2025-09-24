package com.practice.server.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowReview {

    private Long id;

    private Double overallRating;

    private String comment;

    private LocalDateTime createdAt;

    private String username;

    private byte[] profileImg;

    private List<ShowCriteria> criteriaRatings;

}