package com.practice.server.application.controller.api;

import com.practice.server.application.model.entity.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.practice.server.application.constants.Constants.SERVICES_API_BASE;

@RequestMapping(SERVICES_API_BASE)
public interface IServiceController {

    @GetMapping("/{hotelId}")
    ResponseEntity<List<Service>> getServicesByHotel(@PathVariable Long hotelId);

}
