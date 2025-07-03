package com.practice.server.application.service;

import com.practice.server.application.dto.ShowReview;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.model.entity.Review;
import com.practice.server.application.model.entity.User;
import com.practice.server.application.repository.ReviewRepository;
import com.practice.server.application.repository.UsersRepository;
import com.practice.server.application.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    private final UsersRepository usersRepository;

    @Override
    public ReviewResponse reviewByHotelId(Long hotelId) {
        List<Review> reviews = reviewRepository.findByHotelId(hotelId);

        List<ShowReview> showReviews = new ArrayList<>();
        for (Review review : reviews) {
            String email = review.getUser().getEmail();
            Optional<User> userOpt = usersRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                UserDTO userDTO = mapToUserDTO(userOpt.get());
                ShowReview showReview = new ShowReview(review, userDTO);
                showReviews.add(showReview);
            } else {
                ShowReview showReview = new ShowReview(review, null);
                showReviews.add(showReview);
            }
        }
        return new ReviewResponse(0, "Reviews found", LocalDateTime.now(), showReviews);
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
