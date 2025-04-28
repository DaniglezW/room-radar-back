package com.practice.server.application.service.interfaces;

import com.practice.server.application.model.entity.HotelImage;

import java.util.List;

public interface IHotelImageService {

    List<HotelImage> getImagesByHotelId(Long hotelId);

    HotelImage addImageToHotel(Long hotelId, HotelImage hotelImage);

    List<HotelImage> addMultipleImagesToHotel(Long hotelId, List<HotelImage> hotelImages);

    void deleteImageFromHotel(Long hotelId, Long imageId);

    void deleteAllImagesFromHotel(Long hotelId);

}
