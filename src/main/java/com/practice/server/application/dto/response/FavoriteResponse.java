package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Favorite;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FavoriteResponse extends PracticeResponse {

    List<Favorite> favorites;

    public FavoriteResponse(int code, String message, LocalDateTime ts, List<Favorite> favorites) {
        super(code, message, ts);
        this.favorites = favorites;
    }

    public FavoriteResponse(int code, String message, LocalDateTime ts) {
        super(code, message, ts);
    }

}
