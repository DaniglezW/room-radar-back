package com.practice.server.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {

    @NotBlank
    private String refreshToken;

}
