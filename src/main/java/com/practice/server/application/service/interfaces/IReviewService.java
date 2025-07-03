package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.response.ReviewResponse;

public interface IReviewService {

    ReviewResponse reviewByHotelId(Long hotelId);

}
