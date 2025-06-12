package com.practice.server.application.service;

import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.model.entity.HotelImage;
import com.practice.server.application.repository.HotelImageRepository;
import com.practice.server.application.repository.HotelRepository;
import com.practice.server.application.service.interfaces.IHotelImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HotelImageService implements IHotelImageService {

    private final HotelImageRepository hotelImageRepository;

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelImageService(HotelImageRepository hotelImageRepository, HotelRepository hotelRepository) {
        this.hotelImageRepository = hotelImageRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelImage> getImagesByHotelId(Long hotelId) {
        return hotelImageRepository.findByHotelId(hotelId);
    }

    @Override
    public HotelImage addImageToHotel(Long hotelId, HotelImage hotelImage) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotelImage.setHotel(hotel);
        return hotelImageRepository.save(hotelImage);
    }

    @Override
    public List<HotelImage> addMultipleImagesToHotel(Long hotelId, List<HotelImage> hotelImages) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotelImages.forEach(image -> image.setHotel(hotel));
        return hotelImageRepository.saveAll(hotelImages);
    }

    @Override
    public void deleteImageFromHotel(Long hotelId, Long imageId) {
        HotelImage image = hotelImageRepository.findByIdAndHotelId(imageId, hotelId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        hotelImageRepository.delete(image);
    }

    @Override
    public void deleteAllImagesFromHotel(Long hotelId) {
        hotelImageRepository.deleteByHotelId(hotelId);
    }

}
