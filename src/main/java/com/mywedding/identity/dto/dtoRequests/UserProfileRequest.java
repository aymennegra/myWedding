package com.mywedding.identity.dto.dtoRequests;


import lombok.Data;

@Data
public class UserProfileRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}
