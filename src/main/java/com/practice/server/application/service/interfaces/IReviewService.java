package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.request.CreateReviewRequest;
import com.practice.server.application.dto.response.CriteriaDefinitionResponse;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.dto.response.ReviewStatsResponse;

import java.util.List;

public interface IReviewService {

    ReviewResponse reviewByHotelId(Long hotelId);

    Boolean canUserReviewHotel(Long hotelId, String token);

    ReviewResponse createReview(String token, CreateReviewRequest request);

    void deleteReview(Long id, String token);

    List<CriteriaDefinitionResponse> getCriteriaByHotel(Long hotelId);

    ReviewStatsResponse getHotelReviewStats(Long hotelId);

}
