package com.unitime.unitime_backend.dto.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponseDTO {

    private String token;
    private long expiration;

    public JwtResponseDTO(
            String token,
            long expiration
    ) {
        this.token = token;
        this.expiration = expiration;
    }
}
