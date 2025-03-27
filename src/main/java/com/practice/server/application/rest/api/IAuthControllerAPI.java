package com.practice.server.application.rest.api;

import com.practice.server.application.request.LoginRequest;
import com.practice.server.application.request.RegisterRequest;
import com.practice.server.application.response.PracticeResponse;
import com.practice.server.application.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.practice.server.application.constants.Constants.AUTH;

@RequestMapping(AUTH)
public interface IAuthControllerAPI {

    @PostMapping("/register")
    ResponseEntity<PracticeResponse> register(@RequestBody RegisterRequest request);

    @PostMapping("/login")
    ResponseEntity<PracticeResponse> login(@RequestBody LoginRequest request, HttpServletResponse response);

    @PostMapping("/refresh")
    ResponseEntity<PracticeResponse> refresh(String token, HttpServletResponse response);

    @PostMapping("/verify")
    ResponseEntity<Boolean> verify(String token);

    @GetMapping("/me")
    ResponseEntity<UserResponse> getCurrentUser(String token);

}
