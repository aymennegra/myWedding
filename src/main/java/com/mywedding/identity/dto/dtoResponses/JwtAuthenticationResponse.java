package com.mywedding.identity.dto.dtoResponses;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String refreshToken;
    private String accessToken;
}
