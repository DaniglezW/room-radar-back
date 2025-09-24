package com.practice.server.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatsResponse {

    private Double overallAverage;
    private Map<String, Double> criteriaAverages;
    private Long totalReviews;

}
