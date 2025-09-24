package com.practice.server.application.service;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.CreateReviewRequest;
import com.practice.server.application.dto.response.*;
import com.practice.server.application.model.entity.*;
import com.practice.server.application.repository.*;
import com.practice.server.application.service.interfaces.IReviewService;
import com.practice.server.application.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    private final UsersRepository usersRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final ReservationRepository reservationRepository;

    private final HotelRepository hotelRepository;

    private final CriteriaDefinitionRepository criteriaDefinitionRepository;

    private final ReviewCriteriaRepository reviewCriteriaRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UsersRepository usersRepository, JwtTokenProvider jwtTokenProvider, UserService userService, ReservationRepository reservationRepository, HotelRepository hotelRepository, CriteriaDefinitionRepository criteriaDefinitionRepository, ReviewCriteriaRepository reviewCriteriaRepository) {
        this.reviewRepository = reviewRepository;
        this.usersRepository = usersRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.criteriaDefinitionRepository = criteriaDefinitionRepository;
        this.reviewCriteriaRepository = reviewCriteriaRepository;
    }

    @Override
    public ReviewResponse reviewByHotelId(Long hotelId) {
        List<Review> reviews = reviewRepository.findByHotelId(hotelId);

        List<ShowReview> showReviews = reviews.stream()
                .map(review -> {
                    User user = review.getUser();

                    // Mapear criterios
                    List<ShowCriteria> criteriaRatings = review.getCriteriaRatings().stream()
                            .map(rc -> new ShowCriteria(
                                    rc.getCriteriaDefinition().getId(),
                                    rc.getCriteriaDefinition().getName(),
                                    rc.getRating()
                            ))
                            .toList();

                    // Construir ShowReview
                    return ShowReview.builder()
                            .id(review.getId())
                            .overallRating(review.getOverallRating())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .username(user != null ? user.getFullName() : null)
                            .profileImg(user != null ? user.getProfilePicture() : null)
                            .criteriaRatings(criteriaRatings)
                            .build();
                })
                .toList();

        return new ReviewResponse(0, "Reviews found", LocalDateTime.now(), showReviews);
    }

    @Override
    public Boolean canUserReviewHotel(Long hotelId, String token) {
        Long userId = getUserIdByToken(token);
        return reservationRepository.existsByUserIdAndHotelIdAndCheckOutDateBefore(
                userId, hotelId, LocalDate.now()
        );
    }

    @Override
    public ReviewResponse createReview(String token, CreateReviewRequest request) {
        Long userId = getUserIdByToken(token);
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        // Validar que no exista review previa
        reviewRepository.findByHotelIdAndUserId(hotel.getId(), user.getId())
                .ifPresent(r -> { throw new RuntimeException("El usuario ya dejó una review para este hotel"); });

        Review review = Review.builder()
                .user(user)
                .hotel(hotel)
                .comment(request.getComment())
                .overallRating(request.getOverallRating())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Guardar criterios asociados
        List<ReviewCriteria> criteriaRatings = request.getCriteriaRatings().stream().map(c -> {
            CriteriaDefinition definition = criteriaDefinitionRepository.findById(c.getCriteriaId())
                    .orElseThrow(() -> new RuntimeException("Criterio no válido"));
            return ReviewCriteria.builder()
                    .review(savedReview)
                    .criteriaDefinition(definition)
                    .rating(c.getRating())
                    .build();
        }).toList();

        reviewCriteriaRepository.saveAll(criteriaRatings);
        review.setCriteriaRatings(criteriaRatings);

        return new ReviewResponse(200, "Review creada", LocalDateTime.now(), List.of(mapToShowReview(review, user)));
    }

    @Override
    public void deleteReview(Long id, String token) {
        Long userId = getUserIdByToken(token);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("No puedes eliminar esta review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<CriteriaDefinitionResponse> getCriteriaByHotel(Long hotelId) {
        List<CriteriaDefinition> criteria = criteriaDefinitionRepository.findByActiveTrue();
        return criteria.stream()
                .map(c -> new CriteriaDefinitionResponse(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public ReviewStatsResponse getHotelReviewStats(Long hotelId) {
        List<Review> reviews = reviewRepository.findByHotelId(hotelId);

        if (reviews.isEmpty()) {
            return new ReviewStatsResponse(0.0, Map.of(), 0L);
        }

        // Promedio global
        Double overallAverage = reviews.stream()
                .mapToDouble(Review::getOverallRating)
                .average()
                .orElse(0.0);

        // Promedios por criterio
        List<ReviewCriteria> allCriteria = reviewCriteriaRepository.findByReviewHotelId(hotelId);
        Map<String, Double> criteriaAverages = allCriteria.stream()
                .collect(Collectors.groupingBy(
                        rc -> rc.getCriteriaDefinition().getName(),
                        Collectors.averagingDouble(ReviewCriteria::getRating)
                ));

        return new ReviewStatsResponse(overallAverage, criteriaAverages, (long) reviews.size());
    }

    private Long getUserIdByToken(String token) {
        if (token == null || token.isBlank()) {
            return 0L;
        }
        String email;
        try {
            email = jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            return 0L;
        }

        UserDTO userDTO = userService.getUserByEmail(email);
        return userDTO.getId();
    }

    // 🔹 Auxiliar: mapear Review a ShowReview
    private ShowReview mapToShowReview(Review review, User user) {
        return ShowReview.builder()
                .id(review.getId())
                .overallRating(review.getOverallRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .username(user != null ? user.getFullName() : null)
                .profileImg(user != null ? user.getProfilePicture() : null)
                .criteriaRatings(
                        review.getCriteriaRatings().stream()
                                .map(rc -> new ShowCriteria(
                                        rc.getCriteriaDefinition().getId(),
                                        rc.getCriteriaDefinition().getName(),
                                        rc.getRating()
                                ))
                                .toList()
                )
                .build();
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
