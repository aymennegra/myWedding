package com.mywedding.identity.controller;

import com.mywedding.identity.dto.dtoRequests.UserProfileRequest;
import com.mywedding.identity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile() {
        return userService.getUserProfile();
    }

    @PutMapping("/update")
    public ResponseEntity<Object>  updateUserProfile(@RequestBody UserProfileRequest userProfileRequest) {
        // Call the updateUserProfile() method from AuthenticationService and return the result
        return userService.updateUserProfile(userProfileRequest);
    }
}
