package com.mywedding.identity.services.impl;

import com.mywedding.identity.dto.dtoRequests.UserProfileRequest;
import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.dto.dtoResponses.UpdateProfileResponse;
import com.mywedding.identity.dto.dtoResponses.UserProfileResponse;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.identity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
    }

    public ResponseEntity<Object> getUserProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Check if a user with the provided email already exists
            try {
                // Retrieve the currently authenticated user's details from the security context
                // Check if the user exists in the database based on the email (assuming email is the username)
                User user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                // Construct UserProfileResponse from user details
                UserProfileResponse userProfileResponse = new UserProfileResponse();
                userProfileResponse.setId(String.valueOf(user.getUser_id()));
                userProfileResponse.setFirstname(user.getFirstname());
                userProfileResponse.setLastname(user.getLastname());
                userProfileResponse.setEmail(user.getEmail());
                userProfileResponse.setPhone(user.getPhone());
                // Add other profile information as needed
                return ResponseHandler.responseBuilder("User found", HttpStatus.OK,
                        userProfileResponse);
            }catch (Exception e){
                return ResponseHandler.responseBuilder("User not found", HttpStatus.UNAUTHORIZED,
                        new ArrayList<>());
            }
        }

    public ResponseEntity<Object> updateUserProfile(UserProfileRequest userProfileRequest) {
        try {// Retrieve the currently authenticated user's details from the security context
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<User> existingUserOptional = userRepository.findByEmail(userProfileRequest.getEmail());

            // Update user information with the data from the userProfileRequest if not null
            if (userProfileRequest.getFirstname() == null) {
                user.setFirstname(user.getFirstname());
            }else {
                user.setFirstname(userProfileRequest.getFirstname());
            }

            if (userProfileRequest.getLastname() == null) {
                user.setLastname(user.getLastname());
            }else {
                user.setLastname(userProfileRequest.getLastname());
            }

            if (userProfileRequest.getEmail() == null ) {
                user.setEmail(user.getEmail());
            }else if (existingUserOptional.isEmpty())
            {
                user.setEmail(userProfileRequest.getEmail());
            } else {
                return ResponseHandler.responseBuilder("sorry ,user already exists", HttpStatus.UNAUTHORIZED,
                        new ArrayList<>());
            }

            if (userProfileRequest.getPhone() == null) {
                user.setPhone(user.getPhone());
            }else {
                user.setPhone(userProfileRequest.getPhone());
            }

            if (userProfileRequest.getPassword() == null) {
                // Update password only if not null
                user.setPassword(user.getPassword());
            }else{
                user.setPassword(new BCryptPasswordEncoder().encode(userProfileRequest.getPassword()));
            }

            // Save the updated user entity
            userRepository.save(user);

            // Construct and return a UserProfileResponse with updated user information
            UpdateProfileResponse userProfileResponse = new UpdateProfileResponse();
            userProfileResponse.setFirstname(user.getFirstname());
            userProfileResponse.setLastname(user.getLastname());
            userProfileResponse.setEmail(user.getEmail());
            userProfileResponse.setPhone(user.getPhone());
            // Add other profile information as needed
            return ResponseHandler.responseBuilder("User Edited", HttpStatus.OK,
                    userProfileResponse);
        }
        catch (Exception e){
            return ResponseHandler.responseBuilder("An error has occurred", HttpStatus.UNAUTHORIZED,
                    new ArrayList<>());
        }
    }
}


