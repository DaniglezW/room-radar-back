package com.practice.server.application.controller.room;

import com.practice.server.application.controller.api.IRoomImageController;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.model.entity.RoomImage;
import com.practice.server.application.service.interfaces.IRoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomImageController implements IRoomImageController {

    private final IRoomImageService roomImageService;

    @Override
    public ResponseEntity<List<RoomImage>> getImagesByRoom(Long roomId) {
        return ResponseEntity.ok(roomImageService.getImagesByRoom(roomId));
    }

    @Override
    public ResponseEntity<RoomImage> addImageToRoom(Long roomId, RoomImage image) {
        return ResponseEntity.ok(roomImageService.addImageToRoom(roomId, image));
    }

    @Override
    public ResponseEntity<List<RoomImage>> addMultipleImagesToRoom(Long roomId, List<RoomImage> images) {
        return ResponseEntity.ok(roomImageService.addMultipleImagesToRoom(roomId, images));
    }

    @Override
    public ResponseEntity<PracticeResponse> deleteImage(Long imageId) {
        roomImageService.deleteImage(imageId);
        return ResponseEntity.ok(new PracticeResponse(0, "Imagen eliminada", LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<PracticeResponse> deleteMultipleImages(List<Long> ids) {
        roomImageService.deleteMultipleImages(ids);
        return ResponseEntity.ok(new PracticeResponse(0, "Imágenes eliminadas", LocalDateTime.now()));
    }

}
