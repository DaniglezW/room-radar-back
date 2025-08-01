package com.practice.server.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLoginRequest {

    @NotBlank
    private String provider; // "google", "microsoft"

    @NotBlank
    private String idToken;

}
