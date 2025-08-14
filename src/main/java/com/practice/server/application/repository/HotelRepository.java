package com.practice.server.application.repository;

import com.practice.server.application.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
    SELECT DISTINCT h
    FROM Hotel h
    JOIN h.rooms r
    WHERE
        (
          :name IS NULL OR
          LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')) OR
          LOWER(h.city) LIKE LOWER(CONCAT('%', :name, '%')) OR
          LOWER(h.country) LIKE LOWER(CONCAT('%', :name, '%'))
        )
        AND (:maxGuests IS NULL OR r.maxGuests >= :maxGuests)
        AND r.available = TRUE
        AND (
            (:checkIn IS NULL OR :checkOut IS NULL) OR
            NOT EXISTS (
                SELECT 1
                FROM Reservation res
                WHERE res.room = r
                  AND res.status IN (com.practice.server.application.model.enums.ReservationStatus.PENDING, com.practice.server.application.model.enums.ReservationStatus.CONFIRMED)
                  AND res.checkInDate < :checkOut
                  AND res.checkOutDate > :checkIn
            )
        )
""")
    List<Hotel> searchHotels(
            @Param("name") String name,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("maxGuests") Integer maxGuests
    );

    @Query("""
    SELECT h FROM Hotel h
    WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(h.city) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(h.country) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Hotel> searchByNameCityOrCountry(@Param("query") String query);

    @Query("""
    SELECT h
    FROM Hotel h
    JOIN Review r ON r.hotel = h
    GROUP BY h
    ORDER BY AVG(r.rating) DESC
    """)
    List<Hotel> findTopRatedHotels(Pageable pageable);

    @Query("""
        SELECT h
        FROM Hotel h
        JOIN Favorite f ON f.hotel = h
        GROUP BY h
        ORDER BY COUNT(f.id) DESC
    """)
    List<Hotel> findMostFavoritedHotels(Pageable pageable);

    List<Hotel> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
        SELECT h.city, COUNT(r.id)
        FROM Hotel h
        JOIN Review r ON r.hotel = h
        GROUP BY h.city
        ORDER BY COUNT(r.id) ASC
    """)
    List<Object[]> findTopCitiesByReviewCount(Pageable pageable);

    @Query("""
        SELECT h.country, COUNT(r.id)
        FROM Hotel h
        LEFT JOIN Review r ON r.hotel = h
        GROUP BY h.country
        ORDER BY COUNT(r.id) ASC
    """)
    List<Object[]> findTopCountryByReviewCount(Pageable pageable);

    @Query("""
        SELECT DISTINCT f2.hotel
        FROM Favorite f1
        JOIN Favorite f2 ON f1.hotel = f2.hotel AND f1.user != f2.user
        WHERE f1.user.id = :userId
    """)
    List<Hotel> findRecommendedHotelsForUser(@Param("userId") Long userId);

    List<Hotel> findByStarsGreaterThanEqualAndCity(Integer stars, String city);

    Long countByCountry(String country);

}
