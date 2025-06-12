package com.practice.server.application.controller.service;

import com.practice.server.application.controller.api.IServiceController;
import com.practice.server.application.model.entity.Service;
import com.practice.server.application.service.interfaces.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServiceController implements IServiceController {

    private final IService iService;

    @Override
    public ResponseEntity<List<Service>> getServicesByHotel(Long hotelId) {
        return ResponseEntity.ok(iService.getHotelServices(hotelId));
    }

}
