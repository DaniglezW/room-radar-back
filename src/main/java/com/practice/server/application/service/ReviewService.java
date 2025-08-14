package com.practice.server.application.service;

import com.practice.server.application.dto.ShowReview;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.response.ReviewResponse;
import com.practice.server.application.model.entity.Review;
import com.practice.server.application.model.entity.User;
import com.practice.server.application.repository.ReservationRepository;
import com.practice.server.application.repository.ReviewRepository;
import com.practice.server.application.repository.UsersRepository;
import com.practice.server.application.service.interfaces.IReviewService;
import com.practice.server.application.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    private final UsersRepository usersRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UsersRepository usersRepository, JwtTokenProvider jwtTokenProvider, UserService userService, ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.usersRepository = usersRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.reservationRepository = reservationRepository;
    }

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

    @Override
    public Boolean canUserReviewHotel(Long hotelId, String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        String email;
        try {
            email = jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            return false;
        }

        UserDTO userDTO = userService.getUserByEmail(email);
        return reservationRepository.existsByUserIdAndHotelIdAndCheckOutDateBefore(
                userDTO.getId(), hotelId, LocalDate.now()
        );
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
