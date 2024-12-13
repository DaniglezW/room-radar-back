package com.practice.server.domain.services.interfaces;

import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RegisterRequest;

public interface IUserService {

    void register(RegisterRequest request);
    String login(LoginRequest request);
    String refreshToken(String refreshToken);

}
