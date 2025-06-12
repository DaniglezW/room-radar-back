package com.practice.server.application.controller.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.model.entity.RoomImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.practice.server.application.constants.Constants.ROOM_IMAGE_API_BASE;

@RequestMapping(ROOM_IMAGE_API_BASE)
public interface IRoomImageController {

    @GetMapping("/room/{roomId}")
    ResponseEntity<List<RoomImage>> getImagesByRoom(@PathVariable Long roomId);

    @Authenticated
    @PostMapping("/room/{roomId}")
    ResponseEntity<RoomImage> addImageToRoom(@PathVariable Long roomId, @RequestBody RoomImage image);

    @Authenticated
    @PostMapping("/room/{roomId}/batch")
    ResponseEntity<List<RoomImage>> addMultipleImagesToRoom(@PathVariable Long roomId, @RequestBody List<RoomImage> images);

    @Authenticated
    @DeleteMapping("/{imageId}")
    ResponseEntity<PracticeResponse> deleteImage(@PathVariable Long imageId);

    @Authenticated
    @DeleteMapping("/batch")
    ResponseEntity<PracticeResponse> deleteMultipleImages(@RequestBody List<Long> ids);

}
