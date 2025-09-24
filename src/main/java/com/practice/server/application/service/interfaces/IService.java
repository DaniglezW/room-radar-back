package com.practice.server.application.service.interfaces;

import com.practice.server.application.model.entity.Service;

import java.util.List;

public interface IService {

    List<Service> getHotelServices (Long hotelId);

    List<Service> getAllServices();

}
