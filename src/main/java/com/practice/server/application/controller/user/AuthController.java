package com.practice.server.application.controller.user;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.constants.Constants;
import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RegisterRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.dto.response.UserResponse;
import com.practice.server.application.controller.api.IAuthControllerAPI;
import com.practice.server.application.utils.JwtTokenProvider;
import com.practice.server.application.service.interfaces.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
public class AuthController implements IAuthControllerAPI {

    private final IUserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(IUserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<PracticeResponse> register(@RequestBody RegisterRequest request) {
        log.info("Entering into register controller");
        try {
            userService.register(request);
            return ResponseEntity.ok(new PracticeResponse(0, "User registered successfully"));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during registration: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PracticeResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("Entering into login controller");
        try {
            String token = userService.login(request);
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok(new PracticeResponse(0,"Successful login"));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during login: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PracticeResponse> refresh(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
        log.info("Entering into refresh controller");
        try {
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                String newToken = userService.refreshToken(token);
                response.setHeader("Set-Cookie", "token=" + newToken + "; Secure; SameSite=Strict; Path=/; Max-Age=3600");
                return ResponseEntity.ok(new PracticeResponse(0, "Refresh Token Correctly"));
            }
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, "Error during token refresh: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Boolean> verify(@CookieValue(value = "token", required = false) String token) {
        try {
            return token != null && userService.validateToken(token) ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @Override
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue(value = "token", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponse(401, "Token missing"));
        }
        String email;
        try {
            email = jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponse(401, "Invalid token"));
        }

        UserDTO userDTO = userService.getUserByEmail(email);
        UserResponse response = new UserResponse(200, "User found");
        response.setUser(userDTO);

        log.info("User : " + userDTO);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

}
