package com.practice.server.application.service;

import com.practice.server.application.dto.HotelWithRoomsDTO;
import com.practice.server.application.dto.LocationSuggestionDto;
import com.practice.server.application.dto.RoomViewDto;
import com.practice.server.application.dto.request.HotelSearchRequest;
import com.practice.server.application.dto.response.CountryAccommodationResponse;
import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.model.entity.HotelImage;
import com.practice.server.application.service.interfaces.IHotelService;
import com.practice.server.application.repository.HotelRepository;
import com.practice.server.application.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class HotelService implements IHotelService {

    private static final String HOTEL_404_MSG = "Hotel not found for this id :: ";

    private static final String HOTEL404 = "No hotels found";

    private static final String HOTEL_SUCCESSFULLY = "Hotels retrieved successfully";

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelListResponse getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));
        return new HotelResponse(200, "Hotel found", LocalDateTime.now(), hotel);
    }

    @Override
    public HotelResponse createHotel(Hotel hotel) {
        hotel.setCreatedAt(LocalDateTime.now());
        Hotel createdHotel = hotelRepository.save(hotel);
        return new HotelResponse(201, "Hotel created successfully", LocalDateTime.now(), createdHotel);
    }

    @Override
    public HotelResponse updateHotel(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));

        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setCity(hotelDetails.getCity());
        hotel.setCountry(hotelDetails.getCountry());
        hotel.setStars(hotelDetails.getStars());
        hotel.setImages(hotelDetails.getImages());

        Hotel updatedHotel = hotelRepository.save(hotel);

        return new HotelResponse(200, "Hotel updated successfully", LocalDateTime.now(), updatedHotel);
    }

    @Override
    public HotelResponse deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));
        hotelRepository.delete(hotel);
        return new HotelResponse(200, "Hotel deleted successfully", LocalDateTime.now(), hotel);
    }

    @Override
    public List<LocationSuggestionDto> search(String query) {
        List<Hotel> matches = hotelRepository.searchByNameCityOrCountry(query);

        Set<LocationSuggestionDto> suggestions = new LinkedHashSet<>();

        for (Hotel hotel : matches) {
            if (hotel.getName().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(new LocationSuggestionDto("hotel", hotel.getName(), hotel.getCity(), hotel.getCountry()));
            }
            if (hotel.getCity().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(new LocationSuggestionDto("city", hotel.getCity(), hotel.getCity(), hotel.getCountry()));
            }
            if (hotel.getCountry().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(new LocationSuggestionDto("country", hotel.getCountry(), null, hotel.getCountry()));
            }
        }

        return suggestions.stream().limit(10).toList();
    }

    @Override
    public List<HotelWithRoomsDTO> searchHotels(HotelSearchRequest request) {
        String name = StringUtils.hasText(request.getName()) ? request.getName() : null;
        int maxGuests = request.getMaxGuests() != null ? request.getMaxGuests() : 1;

        List<Hotel> hotels = hotelRepository.searchHotels(
                name,
                request.getCheckInDate(),
                request.getCheckOutDate(),
                maxGuests
        );

        return hotels.stream().map(hotel -> {
            List<RoomViewDto> availableRooms = hotel.getRooms().stream()
                    .filter(room -> Boolean.TRUE.equals(room.getAvailable()))
                    .filter(room -> room.getMaxGuests() >= maxGuests)
                    .map(room -> RoomViewDto.builder()
                            .id(room.getId())
                            .type(room.getType())
                            .pricePerNight(room.getPricePerNight())
                            .maxGuests(room.getMaxGuests())
                            .mainImageData(Utils.getMainRoomImageData(room.getImages()))
                            .build())
                    .toList();

            return HotelWithRoomsDTO.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .address(hotel.getAddress())
                    .city(hotel.getCity())
                    .country(hotel.getCountry())
                    .description(hotel.getDescription())
                    .stars(hotel.getStars())
                    .images(hotel.getImages().stream()
                            .map(img -> HotelImage.builder()
                                    .id(img.getId())
                                    .imageData(img.getImageData())
                                    .description(img.getDescription())
                                    .isMain(img.getIsMain())
                                    .build())
                            .toList())
                    .availableRooms(availableRooms)
                    .build();
        }).toList();
    }


    @Override
    public HotelListResponse getTopRatedHotels(int limit) {
        List<Hotel> hotels = hotelRepository.findTopRatedHotels(PageRequest.of(0, limit));
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public HotelListResponse getMostFavoritedHotels(int limit) {
        List<Hotel> hotels = hotelRepository.findMostFavoritedHotels(PageRequest.of(0, limit));
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public HotelListResponse getLatestHotels(int limit) {
        List<Hotel> hotels = hotelRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public List<String> getPopularDestinations(int limit) {
        List<Object[]> results = hotelRepository.findTopCountryByReviewCount(PageRequest.of(0, limit));
        return results.stream().map(obj -> (String) obj[0]).toList();
    }

    @Override
    public HotelListResponse getRecommendedHotels(Long userId) {
        List<Hotel> hotels = hotelRepository.findRecommendedHotelsForUser(userId);
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public HotelListResponse getLuxuryHotelsInCity(String city, int minStars) {
        List<Hotel> hotels = hotelRepository.findByStarsGreaterThanEqualAndCity(minStars, city);
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, HOTEL404, LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), hotels);
    }

    @Override
    public List<CountryAccommodationResponse> getAccommodationCount(List<String> countries) {
        return countries.stream()
                .map(country -> new CountryAccommodationResponse(200, HOTEL_SUCCESSFULLY, LocalDateTime.now(), country, hotelRepository.countByCountry(country)))
                .toList();
    }


}
