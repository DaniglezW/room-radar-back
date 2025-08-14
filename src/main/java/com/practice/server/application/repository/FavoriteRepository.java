package com.practice.server.application.repository;

import com.practice.server.application.model.entity.Favorite;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserAndHotel(User user, Hotel hotel);

    void deleteAllByUser(User user);

}
