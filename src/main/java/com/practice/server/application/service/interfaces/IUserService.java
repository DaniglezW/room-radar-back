package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RegisterRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface IUserService {

    void register(RegisterRequest request);
    String login(LoginRequest request);
    String refreshToken(String refreshToken);
    Boolean validateToken(String token);
    UserDTO getUserByUsername(String username);
    UserDTO getUserByEmail(String email);
    PracticeResponse handleGoogleLoginOrRegister(String idToken, HttpServletResponse response);
    UserDTO getUserByToken (String token);

}
