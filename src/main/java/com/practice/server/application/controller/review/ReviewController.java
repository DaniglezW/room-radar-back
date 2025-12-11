package com.practice.server.application.controller.review;

import com.practice.server.application.controller.api.IReviewControllerAPI;
import com.practice.server.application.dto.request.CreateReviewRequest;
import com.practice.server.application.dto.response.CriteriaDefinitionResponse;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.dto.response.ReviewStatsResponse;
import com.practice.server.application.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController implements IReviewControllerAPI {

    private final IReviewService iReviewService;

    @Override
    public ResponseEntity<ReviewResponse> getById(Long id) {
        return ResponseEntity.ok(iReviewService.reviewByHotelId(id));
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> isReviewAllowed(
            @RequestParam Long hotelId,
            String token
    ) {
        boolean canReview = iReviewService.canUserReviewHotel(hotelId, token);
        return ResponseEntity.ok(Map.of("canReview", canReview));
    }

    @Override
    public ResponseEntity<ReviewResponse> createReview(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateReviewRequest request
    ) {
        ReviewResponse response = iReviewService.createReview(token, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String token
    ) {
        iReviewService.deleteReview(reviewId, token);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CriteriaDefinitionResponse>> getCriterials() {
        return ResponseEntity.ok(iReviewService.getCriterials());
    }

    @Override
    public ResponseEntity<ReviewStatsResponse> getHotelReviewStats(
            @PathVariable Long hotelId
    ) {
        ReviewStatsResponse response = iReviewService.getHotelReviewStats(hotelId);
        return ResponseEntity.ok(response);
    }


}
