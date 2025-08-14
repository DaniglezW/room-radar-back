package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.response.FavoriteResponse;
import com.practice.server.application.model.entity.Favorite;

public interface IFavoriteService {

    FavoriteResponse getFavorites(UserDTO user);

    Favorite addFavorite(UserDTO user, Long hotelId);

    void removeFavorite(UserDTO userDTO, Long hotelId);

    void removeAllFavorites(UserDTO userDTO);

}
