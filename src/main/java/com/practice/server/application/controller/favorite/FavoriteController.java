package com.practice.server.application.controller.favorite;

import com.practice.server.application.constants.Constants;
import com.practice.server.application.controller.api.IFavoriteController;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.FavoriteRequest;
import com.practice.server.application.dto.response.FavoriteResponse;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.model.entity.Favorite;
import com.practice.server.application.service.interfaces.IFavoriteService;
import com.practice.server.application.service.interfaces.IUserService;
import com.practice.server.application.utils.JwtTokenProvider;
import com.practice.server.application.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FavoriteController implements IFavoriteController {

    private final IFavoriteService service;

    private final IUserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<FavoriteResponse> getFavorites(String token) {
        try {
            UserDTO userDTO = userService.getUserByToken(token);
            return ResponseEntity.ok(service.getFavorites(userDTO));
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, MessageUtils.getMessage(Constants.ERROR_CODE));
        }
    }

    @Override
    public ResponseEntity<Favorite> addFavorite(FavoriteRequest favoriteRequest, String token) {
        try {
            UserDTO userDTO = userService.getUserByToken(token);
            return ResponseEntity.ok(service.addFavorite(userDTO, favoriteRequest.getHotelId()));
        } catch (PracticeException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, MessageUtils.getMessage(Constants.ERROR_CODE));
        }
    }

    @Override
    public ResponseEntity<Void> removeFavorite(Long hotelId, String token) {
        try {
            UserDTO userDTO = userService.getUserByToken(token);
            service.removeFavorite(userDTO, hotelId);
            return ResponseEntity.ok().build();
        } catch (PracticeException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, MessageUtils.getMessage(Constants.ERROR_CODE));
        }
    }

    @Override
    public ResponseEntity<Void> removeAllFavorites(String token) {
        try {
            UserDTO userDTO = userService.getUserByToken(token);
            service.removeAllFavorites(userDTO);
            return ResponseEntity.ok().build();
        } catch (PracticeException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PracticeException(Constants.ERROR_CODE, MessageUtils.getMessage(Constants.ERROR_CODE));
        }
    }
}
