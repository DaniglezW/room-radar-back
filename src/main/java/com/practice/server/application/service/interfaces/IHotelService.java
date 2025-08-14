package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.HotelWithRoomsDTO;
import com.practice.server.application.dto.LocationSuggestionDto;
import com.practice.server.application.dto.request.HotelSearchRequest;
import com.practice.server.application.dto.response.CountryAccommodationResponse;
import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.model.entity.Hotel;

import java.util.List;

public interface IHotelService {

    HotelListResponse getAllHotels();

    HotelResponse getHotelById(Long id);

    HotelResponse createHotel(Hotel hotel);

    HotelResponse updateHotel(Long id, Hotel hotel);

    HotelResponse deleteHotel(Long id);

    List<LocationSuggestionDto> search(String query);

    List<HotelWithRoomsDTO> searchHotels(HotelSearchRequest request);

    HotelListResponse getTopRatedHotels(int limit);

    HotelListResponse getMostFavoritedHotels(int limit);

    HotelListResponse getLatestHotels(int limit);

    List<String> getPopularDestinations(int limit);

    HotelListResponse getRecommendedHotels(Long userId);

    HotelListResponse getLuxuryHotelsInCity(String city, int minStars);

    List<CountryAccommodationResponse> getAccommodationCount(List<String> countries);

}
