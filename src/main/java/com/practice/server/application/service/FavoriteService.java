package com.practice.server.application.service;

import com.practice.server.application.constants.Constants;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.response.FavoriteResponse;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.model.entity.Favorite;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.model.entity.User;
import com.practice.server.application.repository.FavoriteRepository;
import com.practice.server.application.repository.HotelRepository;
import com.practice.server.application.repository.UsersRepository;
import com.practice.server.application.service.interfaces.IFavoriteService;
import com.practice.server.application.service.interfaces.IUserService;
import com.practice.server.application.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class FavoriteService implements IFavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final HotelRepository hotelRepository;

    private final UsersRepository usersRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, HotelRepository hotelRepository, UsersRepository usersRepository) {
        this.favoriteRepository = favoriteRepository;
        this.hotelRepository = hotelRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public FavoriteResponse getFavorites(UserDTO user) {
        log.info("Entering into FavoriteService.getFavorites");
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        return new FavoriteResponse(Constants.OK, MessageUtils.getMessage(Constants.OK), LocalDateTime.now(), favorites);
    }

    @Override
    public Favorite addFavorite(UserDTO user, Long hotelId) {
        log.info("Entering into FavoriteService.addFavorite");
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElse(null);
        User responseUser = usersRepository.findByEmail(user.getEmail()).orElse(null);
        if (hotel == null || responseUser == null) {
            throw new PracticeException(Constants.HOTEL_OR_USER_NOT_FOUND, MessageUtils.getMessage(Constants.HOTEL_OR_USER_NOT_FOUND));
        }
        Favorite favorite = Favorite.builder()
                .user(responseUser)
                .hotel(hotel)
                .addedAt(LocalDateTime.now())
                .build();
        return favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(UserDTO userDTO, Long hotelId) {
        log.info("Entering into FavoriteService.removeFavorite");
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElse(null);
        User responseUser = usersRepository.findByEmail(userDTO.getEmail()).orElse(null);
        if (hotel == null || responseUser == null) {
            throw new PracticeException(Constants.HOTEL_OR_USER_NOT_FOUND, MessageUtils.getMessage(Constants.HOTEL_OR_USER_NOT_FOUND));
        }
        Favorite favorite = favoriteRepository.findByUserAndHotel(responseUser, hotel).orElse(null);
        if (favorite == null) {
            throw new PracticeException(Constants.FAVORITE_NOT_FOUND,
                    MessageUtils.getMessage(Constants.FAVORITE_NOT_FOUND));
        }
        favoriteRepository.delete(favorite);
        log.info("Favorite removed for user {} and hotel {}", responseUser.getEmail(), hotel.getId());
    }

    @Override
    public void removeAllFavorites(UserDTO userDTO) {
        log.info("Entering into FavoriteService.removeAllFavorites");
        User responseUser = usersRepository.findByEmail(userDTO.getEmail()).orElse(null);
        if (responseUser == null) {
            throw new PracticeException(Constants.HOTEL_OR_USER_NOT_FOUND, MessageUtils.getMessage(Constants.HOTEL_OR_USER_NOT_FOUND));
        }
        favoriteRepository.deleteAllByUser(responseUser);
        log.info("All favorites removed for user {}", responseUser.getEmail());
    }

}
