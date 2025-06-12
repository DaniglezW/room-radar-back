package com.practice.server.application.repository;

import com.practice.server.application.model.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    List<RoomImage> findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);
    void deleteByIdIn(List<Long> ids);

}
