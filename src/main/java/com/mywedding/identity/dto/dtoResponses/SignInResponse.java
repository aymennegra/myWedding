package com.mywedding.identity.dto.dtoResponses;

import com.mywedding.identity.entities.UserType;
import lombok.Data;

import java.util.Date;

@Data
public class SignInResponse {
    private String firstname;
    private UserType userType;
    private String accessToken;
    private String refreshToken;
    private Date tokenExpirationDate;
}
