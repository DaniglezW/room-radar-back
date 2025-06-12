package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Room;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoomResponse extends PracticeResponse {

    private Room room;

    public RoomResponse(int code, String message, LocalDateTime ts, Room room) {
        super(code, message, ts);
        this.room = room;
    }

}
