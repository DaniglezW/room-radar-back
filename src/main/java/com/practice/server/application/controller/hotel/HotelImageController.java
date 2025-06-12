package com.practice.server.application.controller.hotel;

import com.practice.server.application.controller.api.IHotelImageController;
import com.practice.server.application.dto.response.HotelImageResponse;
import com.practice.server.application.model.entity.HotelImage;
import com.practice.server.application.service.interfaces.IHotelImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class HotelImageController implements IHotelImageController {

    private static final String IMAGES_OK = "Images retrieved successfully";

    private final IHotelImageService hotelImageService;

    @Autowired
    public HotelImageController(IHotelImageService hotelImageService) {
        this.hotelImageService = hotelImageService;
    }

    @Override
    public ResponseEntity<HotelImageResponse> getImagesByHotelId(Long hotelId) {
        List<HotelImage> hotelImages = hotelImageService.getImagesByHotelId(hotelId);

        if (!hotelImages.isEmpty()) {
            return ResponseEntity.ok(new HotelImageResponse(0, IMAGES_OK, LocalDateTime.now(), hotelImages));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new HotelImageResponse(404, "No images found", LocalDateTime.now(), null)));
        }
    }

    @Override
    public ResponseEntity<HotelImageResponse> addImageToHotel(Long hotelId, HotelImage hotelImage) {
        HotelImage savedImage = hotelImageService.addImageToHotel(hotelId, hotelImage);
        List<HotelImage> savedImageList = new ArrayList<>();
        savedImageList.add(savedImage);
        HotelImageResponse response = new HotelImageResponse(0, IMAGES_OK, LocalDateTime.now(), savedImageList);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<HotelImageResponse> addMultipleImagesToHotel(Long hotelId, List<HotelImage> hotelImages) {
        List<HotelImage> savedImages = hotelImageService.addMultipleImagesToHotel(hotelId, hotelImages);
        HotelImageResponse response = new HotelImageResponse(0, IMAGES_OK, LocalDateTime.now(), savedImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteImageFromHotel(Long hotelId, Long imageId) {
        hotelImageService.deleteImageFromHotel(hotelId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllImagesFromHotel(Long hotelId) {
        hotelImageService.deleteAllImagesFromHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

}
