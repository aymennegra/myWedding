package com.mywedding.identity.dto.dtoResponses;

import lombok.Data;

@Data
public class UpdateProfileResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String accessToken;
    private String refreshToken;
}
