package com.mywedding.identity.dto.dtoRequests;


import com.mywedding.identity.entities.Role;
import lombok.Data;

@Data
public class UserProfileRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private Role role;
}
