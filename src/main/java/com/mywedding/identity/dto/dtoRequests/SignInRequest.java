package com.mywedding.identity.dto.dtoRequests;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
