package com.practice.server.application.repository;

import com.practice.server.application.model.entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {

    List<HotelImage> findByHotelId(Long hotelId);

    Optional<HotelImage> findByIdAndHotelId(Long imageId, Long hotelId);

    void deleteByHotelId(Long hotelId);

}
