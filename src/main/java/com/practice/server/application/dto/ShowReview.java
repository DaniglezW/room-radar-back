package com.practice.server.application.dto;


import com.practice.server.application.model.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowReview {

    private Review review;

    private UserDTO user;

}
