package com.practice.server.application.service;

import com.practice.server.application.dto.request.ReservationRequest;
import com.practice.server.application.dto.response.ReservationResponseDTO;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.model.entity.*;
import com.practice.server.application.model.enums.ReservationAction;
import com.practice.server.application.model.enums.ReservationStatus;
import com.practice.server.application.repository.ReservationRepository;
import com.practice.server.application.repository.RoomRepository;
import com.practice.server.application.service.interfaces.IReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    private final AvailabilityService availabilityService;

    private final UserService userService;

    private final ReservationHistoryService reservationHistoryService;

    private final EmailService emailService;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public Reservation update(Long id, Reservation updatedReservation) {
        Reservation existing = findById(id);

        existing.setCheckInDate(updatedReservation.getCheckInDate());
        existing.setCheckOutDate(updatedReservation.getCheckOutDate());
        existing.setGuests(updatedReservation.getGuests());
        existing.setTotalPrice(updatedReservation.getTotalPrice());
        existing.setStatus(updatedReservation.getStatus());
        existing.setRoom(updatedReservation.getRoom());
        existing.setUser(updatedReservation.getUser());

        return reservationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public List<Reservation> findByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    @Override
    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(ReservationStatus.valueOf(status.toUpperCase()));
    }

    @Override
    public Reservation createReservation(ReservationRequest request, String token,  HttpServletRequest httpServletRequest) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }

        if (request.getGuests() > room.getMaxGuests()) {
            throw new PracticeException(2, "Máximo de huéspedes superado");
        }

        boolean available = availabilityService.isAvailable(
                room, request.getCheckInDate(), request.getCheckOutDate());

        if (!available) {
            throw new PracticeException(2, "Habitación no disponible en las fechas indicadas");
        }

        long numberOfNights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        numberOfNights = Math.max(numberOfNights, 1);
        double totalPrice = room.getPricePerNight() * numberOfNights;

        User user = getUserIfLoggedIn(token);

        Reservation reservation = Reservation.builder()
                .room(room)
                .user(user)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .guests(request.getGuests())
                .guestNames(String.join(",", request.getGuestNames()))
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .paymentMethod(request.getPaymentMethod())
                .cardNumber("**** " + request.getCardNumber().substring(request.getCardNumber().length() - 4)) // Solo últimos 4 dígitos
                .totalPrice(totalPrice)
                .status(ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .confirmationCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .hotel(room.getHotel())
                .build();

        Reservation result = reservationRepository.save(reservation);

        emailService.sendHtmlEmail(
                reservation.getGuestEmail(),
                "Tu reserva en " + reservation.getHotel().getName(),
                emailService.buildReservationEmail(reservation.getGuestNames(), reservation)
        );

        ReservationHistory history = ReservationHistory.builder()
                .reservation(reservation)
                .user(user)
                .action(ReservationAction.CREATED)
                .actionDate(LocalDateTime.now())
                .ipAddress(ipAddress)
                .details("Reserva creada: " + reservation.getGuestNames() +
                        ", " + reservation.getGuests() + " huéspedes, total: " + totalPrice + "€")
                .build();
        reservationHistoryService.save(history);
        return result;
    }

    @Override
    public List<ReservationResponseDTO> getMyReservations(String token, String status) {
        User user = userService.getUserFromToken(token);

        List<Reservation> reservations;
        if (status != null) {
            reservations = reservationRepository.findByUserIdAndStatus(user.getId(), ReservationStatus.valueOf(status));
        } else {
            reservations = reservationRepository.findByUserId(user.getId());
        }

        return reservations.stream()
                .map(reservation -> ReservationResponseDTO.builder()
                        .id(reservation.getId())
                        .checkInDate(reservation.getCheckInDate())
                        .checkOutDate(reservation.getCheckOutDate())
                        .guests(reservation.getGuests())
                        .totalPrice(reservation.getTotalPrice())
                        .guestNames(reservation.getGuestNames())
                        .guestEmail(reservation.getGuestEmail())
                        .guestPhone(reservation.getGuestPhone())
                        .paymentMethod(reservation.getPaymentMethod())
                        .cardNumber(reservation.getCardNumber())
                        .confirmationCode(reservation.getConfirmationCode())
                        .status(reservation.getStatus())
                        .createdAt(reservation.getCreatedAt())
                        .hotelId(reservation.getHotel().getId())
                        .hotelName(reservation.getHotel().getName())
                        .mainImage(extractMainImage(reservation.getHotel()))
                        .build())
                .toList();
    }

    private byte[] extractMainImage(Hotel hotel) {
        return hotel.getImages().stream()
                .filter(HotelImage::getIsMain)
                .findFirst()
                .map(HotelImage::getImageData)
                .orElse(null);
    }

    private User getUserIfLoggedIn(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return userService.getUserFromToken(token);
        } catch (PracticeException e) {
            return null;
        }
    }

}
