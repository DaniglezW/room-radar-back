package com.practice.server.domain.services.interfaces;

import com.practice.server.application.response.HotelListResponse;
import com.practice.server.application.response.HotelResponse;
import com.practice.server.domain.model.Hotel;

public interface IHotelService {

    HotelListResponse getAllHotels();

    HotelResponse getHotelById(Long id);

    HotelResponse createHotel(Hotel hotel);

    HotelResponse updateHotel(Long id, Hotel hotel);

    HotelResponse deleteHotel(Long id);

}
