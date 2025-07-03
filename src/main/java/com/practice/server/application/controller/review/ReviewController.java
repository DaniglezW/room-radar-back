package com.practice.server.application.controller.review;

import com.practice.server.application.controller.api.IReviewControllerAPI;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController implements IReviewControllerAPI {

    private final IReviewService iReviewService;

    @Override
    public ResponseEntity<ReviewResponse> getById(Long id) {
        return ResponseEntity.ok(iReviewService.reviewByHotelId(id));
    }

}
