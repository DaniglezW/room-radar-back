package com.practice.server.application.service;

import com.practice.server.application.exception.ResourceNotFoundException;
import com.practice.server.application.model.entity.Room;
import com.practice.server.application.model.entity.RoomImage;
import com.practice.server.application.repository.RoomImageRepository;
import com.practice.server.application.repository.RoomRepository;
import com.practice.server.application.service.interfaces.IRoomImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomImageService implements IRoomImageService {

    private final RoomImageRepository imageRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<RoomImage> getImagesByRoom(Long roomId) {
        return imageRepository.findByRoomId(roomId);
    }

    @Override
    public RoomImage addImageToRoom(Long roomId, RoomImage image) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        image.setRoom(room);
        return imageRepository.save(image);
    }

    @Override
    public List<RoomImage> addMultipleImagesToRoom(Long roomId, List<RoomImage> images) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        images.forEach(img -> img.setRoom(room));
        return imageRepository.saveAll(images);
    }

    @Override
    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }

    @Override
    public void deleteMultipleImages(List<Long> ids) {
        imageRepository.deleteByIdIn(ids);
    }

}
