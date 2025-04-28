package com.practice.server.application.controller.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.dto.response.HotelImageResponse;
import com.practice.server.application.model.entity.HotelImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.practice.server.application.constants.Constants.HOTEL_IMAGE_API_BASE;

@RequestMapping(HOTEL_IMAGE_API_BASE)
public interface IHotelImageController {

    @GetMapping("/{hotelId}")
    ResponseEntity<HotelImageResponse> getImagesByHotelId(@PathVariable Long hotelId);

    @Authenticated
    @PostMapping("/{hotelId}")
    ResponseEntity<HotelImageResponse> addImageToHotel(@PathVariable Long hotelId, @RequestBody HotelImage hotelImage);

    @Authenticated
    @PostMapping("/{hotelId}/bulk")
    ResponseEntity<HotelImageResponse> addMultipleImagesToHotel(@PathVariable Long hotelId, @RequestBody List<HotelImage> hotelImages);

    @Authenticated
    @DeleteMapping("/{hotelId}/{imageId}")
    ResponseEntity<Void> deleteImageFromHotel(@PathVariable Long hotelId, @PathVariable Long imageId);

    @Authenticated
    @DeleteMapping("/{hotelId}")
    ResponseEntity<Void> deleteAllImagesFromHotel(@PathVariable Long hotelId);

}
