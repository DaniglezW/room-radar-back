package com.practice.server.application.service.interfaces;

import com.practice.server.application.model.entity.RoomImage;

import java.util.List;

public interface IRoomImageService {

    List<RoomImage> getImagesByRoom(Long roomId);

    RoomImage addImageToRoom(Long roomId, RoomImage image);

    List<RoomImage> addMultipleImagesToRoom(Long roomId, List<RoomImage> images);

    void deleteImage(Long imageId);

    void deleteMultipleImages(List<Long> ids);

}
