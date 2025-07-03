package com.practice.server.application.controller.api;

import com.practice.server.application.dto.response.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.practice.server.application.constants.Constants.REVIEW_API_BASE;

@RequestMapping(REVIEW_API_BASE)
public interface IReviewControllerAPI {

    @GetMapping("/{id}")
    ResponseEntity<ReviewResponse> getById(@PathVariable Long id);

}
