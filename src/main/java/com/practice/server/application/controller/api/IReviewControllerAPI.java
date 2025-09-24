package com.practice.server.application.controller.api;

import com.practice.server.application.dto.request.CreateReviewRequest;
import com.practice.server.application.dto.response.CriteriaDefinitionResponse;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.dto.response.ReviewStatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.practice.server.application.constants.Constants.REVIEW_API_BASE;

@RequestMapping(REVIEW_API_BASE)
public interface IReviewControllerAPI {

    @GetMapping("/{id}")
    ResponseEntity<ReviewResponse> getById(@PathVariable Long id);

    @GetMapping("/is-allowed")
    ResponseEntity<Map<String, Boolean>> isReviewAllowed(
            @RequestParam Long hotelId,
            String token
    );

    @PostMapping
    ResponseEntity<ReviewResponse> createReview(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateReviewRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/criteria/{hotelId}")
    ResponseEntity<List<CriteriaDefinitionResponse>> getCriteriaByHotel(@PathVariable Long hotelId);

    @GetMapping("/hotel/{hotelId}/stats")
    ResponseEntity<ReviewStatsResponse> getHotelReviewStats(@PathVariable Long hotelId);

}
