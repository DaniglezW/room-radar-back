package com.practice.server.application.rest;

import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.constants.Constants;
import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RefreshRequest;
import com.practice.server.application.dto.request.RegisterRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.rest.api.IAuthControllerAPI;
import com.practice.server.domain.services.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController implements IAuthControllerAPI {

    private final IUserService userService;

    @Autowired
    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<PracticeResponse> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok(new PracticeResponse(0, "User registered successfully"));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during registration: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PracticeResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request);
            return ResponseEntity.ok(new PracticeResponse(0, token));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during login: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PracticeResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String newToken = userService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(new PracticeResponse(0, newToken));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during token refresh: " + e.getMessage());
        }
    }
}
