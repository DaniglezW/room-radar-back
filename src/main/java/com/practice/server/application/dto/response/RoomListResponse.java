package com.practice.server.application.dto.response;

import com.practice.server.application.model.entity.Room;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoomListResponse extends PracticeResponse {

    private List<Room> room;

    public RoomListResponse(int code, String message, LocalDateTime ts, List<Room> room) {
        super(code, message, ts);
        this.room = room;
    }

}