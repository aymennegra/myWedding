package com.mywedding.identity.dto.dtoResponses;

import lombok.Data;

import java.util.Date;

@Data
public class SignInResponse {
    private String firstname;
    private String accessToken;
    private String refreshToken;
    private Date tokenExpirationDate;
}
