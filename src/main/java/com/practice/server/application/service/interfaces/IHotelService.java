package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.model.entity.Hotel;

public interface IHotelService {

    HotelListResponse getAllHotels();

    HotelResponse getHotelById(Long id);

    HotelResponse createHotel(Hotel hotel);

    HotelResponse updateHotel(Long id, Hotel hotel);

    HotelResponse deleteHotel(Long id);

}
