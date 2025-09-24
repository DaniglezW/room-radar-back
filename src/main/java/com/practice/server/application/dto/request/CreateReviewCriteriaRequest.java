package com.practice.server.application.dto.request;

import lombok.Data;

@Data
public class CreateReviewCriteriaRequest {

    private Long criteriaId;
    private Double rating;

}
