package com.practice.server.application.controller.api;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.practice.server.application.constants.Constants.USER;

@RequestMapping(USER)
public interface IUserControllerAPI {

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserRequest request);

}
