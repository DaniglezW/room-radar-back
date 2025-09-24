package com.practice.server.application.controller.user;

import com.practice.server.application.controller.api.IUserControllerAPI;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.UpdateUserRequest;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.service.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController implements IUserControllerAPI {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseEntity<UserDTO> updateUser(UpdateUserRequest request, @CookieValue(value = "token", required = false) String token) {
        log.info("Entering into updateUser controller");
        try {
            return ResponseEntity.ok(userService.updateUser(request, token));
        } catch (PracticeException e) {
            throw e;
        } catch (Exception e) {
            throw new PracticeException(500, "Error en socialLogin: " + e.getMessage());
        }
    }

}
