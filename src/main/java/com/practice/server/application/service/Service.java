package com.practice.server.application.service;

import com.practice.server.application.repository.ServiceRepository;
import com.practice.server.application.service.interfaces.IService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
public class Service implements IService {

    private final ServiceRepository serviceRepository;

    public Service(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.practice.server.application.model.entity.Service> getHotelServices(Long hotelId) {
        List<com.practice.server.application.model.entity.Service> services = serviceRepository.findByHotels_Id(hotelId);
        return services != null ? services : Collections.emptyList();
    }

    @Override
    public List<com.practice.server.application.model.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

}
