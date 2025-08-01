package com.practice.server.application.dto;

import com.practice.server.application.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role role;
    private byte[] profilePicture;
    private String googleProfilePictureUrl;
    private LocalDateTime createdAt;

}
