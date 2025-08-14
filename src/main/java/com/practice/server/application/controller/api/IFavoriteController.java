package com.practice.server.application.controller.api;

import com.practice.server.application.dto.request.FavoriteRequest;
import com.practice.server.application.dto.response.FavoriteResponse;
import com.practice.server.application.model.entity.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.practice.server.application.constants.Constants.FAVORITES_API_BASE;

@RequestMapping(FAVORITES_API_BASE)
public interface IFavoriteController {

    @GetMapping("/favorites")
    ResponseEntity<FavoriteResponse> getFavorites(@CookieValue(value = "token", required = false) String token);

    @PostMapping("/favorites")
    ResponseEntity<Favorite> addFavorite(@RequestBody FavoriteRequest favoriteRequest,
                                         @CookieValue(value = "token", required = false) String token
    );

    @DeleteMapping("/favorites/{hotelId}")
    ResponseEntity<Void> removeFavorite(@PathVariable Long hotelId,
                                        @CookieValue(value = "token", required = false) String token);

    @DeleteMapping("/favorites")
    ResponseEntity<Void> removeAllFavorites(@CookieValue(value = "token", required = false) String token);

}
