package com.practice.server.application.dto.response;

import com.practice.server.application.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends PracticeResponse {

    private UserDTO user;

    public UserResponse(int code, String message) {
        super(code, message);
    }

}
