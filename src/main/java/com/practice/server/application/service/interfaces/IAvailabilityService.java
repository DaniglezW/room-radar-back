package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;

import java.util.List;

public interface IAvailabilityService {

    List<AvailableRoomResponse> checkAvailability(Long hotelId, AvailabilityRequest request);

}
