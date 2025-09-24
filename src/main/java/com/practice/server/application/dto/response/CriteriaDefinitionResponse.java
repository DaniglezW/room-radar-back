package com.practice.server.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaDefinitionResponse {
    private Long id;
    private String name;
}

