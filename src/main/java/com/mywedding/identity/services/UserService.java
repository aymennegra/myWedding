package com.mywedding.identity.services;

import com.mywedding.identity.dto.dtoRequests.UpdatePasswordRequest;
import com.mywedding.identity.dto.dtoRequests.UserProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<Object> getUserProfile ();
    ResponseEntity<Object> updateUserProfile(UserProfileRequest userProfileRequest);
    ResponseEntity<Object> updatePassword (UpdatePasswordRequest updatePasswordRequest);
}
