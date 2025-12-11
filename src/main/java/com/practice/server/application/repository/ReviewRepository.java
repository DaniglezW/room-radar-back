package com.practice.server.application.repository;

import com.practice.server.application.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHotelId(Long hotelId);

    Optional<Review> findByHotelIdAndUserId(Long hotelId, Long userId);

    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);

}
