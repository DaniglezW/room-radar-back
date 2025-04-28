package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.HotelImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HotelImageResponse extends PracticeResponse {

    private List<HotelImage> images;

    public HotelImageResponse(int code, String message, LocalDateTime ts, List<HotelImage> images) {
        super(code, message, ts);
        this.images = images;
    }

}
