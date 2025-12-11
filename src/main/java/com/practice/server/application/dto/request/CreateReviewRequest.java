package com.practice.server.application.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateReviewRequest {

    private Long hotelId;
    private String comment;
    private Double overallRating;
    private List<CreateReviewCriteriaRequest> criteriaRatings;

}
