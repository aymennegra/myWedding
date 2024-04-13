package com.mywedding.identity.dto.dtoResponses;

import com.mywedding.identity.entities.UserType;
import lombok.Data;

@Data
public class SignUpResponse  {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private UserType userType;
    private String accessToken;
    private String refreshToken;
}
