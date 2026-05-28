package com.gamerate.gamerate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private final String type = "Bearer";
    private String username;
    private String role;
}