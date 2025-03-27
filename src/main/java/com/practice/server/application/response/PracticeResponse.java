package com.practice.server.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
@Builder
@AllArgsConstructor
public class PracticeResponse {

    @Schema(description = "Código de resultado",
            example = "0")
    protected int code;

    @Schema(description = "Mensaje descriptivo",
            example = "Operación completada exitosamente")
    protected String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha de finalización",
            example = "2024-05-24T12:00:00")
    protected LocalDateTime ts;

    public PracticeResponse() {}

    public PracticeResponse(int code, String message) {
        this.code = code;
        this.ts = LocalDateTime.now();
        this.message = message;
    }

}