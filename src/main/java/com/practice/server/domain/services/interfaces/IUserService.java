package com.practice.server.domain.services.interfaces;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.request.LoginRequest;
import com.practice.server.application.request.RegisterRequest;

public interface IUserService {

    void register(RegisterRequest request);
    String login(LoginRequest request);
    String refreshToken(String refreshToken);
    Boolean validateToken(String token);
    UserDTO getUserByUsername(String username);

}
