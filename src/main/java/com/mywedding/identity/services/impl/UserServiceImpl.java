package com.mywedding.identity.services.impl;

import com.mywedding.identity.dto.dtoRequests.UpdatePasswordRequest;
import com.mywedding.identity.dto.dtoRequests.UserProfileRequest;
import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.dto.dtoResponses.UpdateProfileResponse;
import com.mywedding.identity.dto.dtoResponses.UserProfileResponse;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.identity.services.JWTService;
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
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;


    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
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
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("User not found", HttpStatus.UNAUTHORIZED,
                    new ArrayList<>());
        }
    }

    public ResponseEntity<Object> updateUserProfile(UserProfileRequest userProfileRequest) {
        try {
            UpdateProfileResponse userProfileResponse = new UpdateProfileResponse();
            // Retrieve the currently authenticated user's details from the security context
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Check if the new email already exists in the database
            Optional<User> existingUserOptional = userRepository.findByEmail(userProfileRequest.getEmail());

            // Update user information with the data from the userProfileRequest if not null
            if (userProfileRequest.getFirstname() == null) {
                user.setFirstname(user.getFirstname());
            } else {
                user.setFirstname(userProfileRequest.getFirstname());
            }

            if (userProfileRequest.getLastname() == null) {
                user.setLastname(user.getLastname());
            } else {
                user.setLastname(userProfileRequest.getLastname());
            }

            if (userProfileRequest.getEmail() == null) {
                user.setEmail(user.getEmail());
            } else if (existingUserOptional.isPresent()) {
                return ResponseHandler.responseBuilder("Sorry, user already exists with this email.", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            } else {
                // Update user's email
                user.setEmail(userProfileRequest.getEmail());

                // Update user's refresh token if email is changed
                String newRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
                String extractedRefreshToken = jwtService.extractRefreshTokenId(newRefreshToken);
                user.setRefreshToken(extractedRefreshToken);
                var jwt = jwtService.generateToken(user);
                userProfileResponse.setAccessToken(jwt);
                userProfileResponse.setRefreshToken(extractedRefreshToken);
            }

            if (userProfileRequest.getPhone() == null) {
                user.setPhone(user.getPhone());
            } else {
                user.setPhone(userProfileRequest.getPhone());
            }

            // Save the updated user entity
            userRepository.save(user);

            // Construct and return a UserProfileResponse with updated user information

            userProfileResponse.setFirstname(user.getFirstname());
            userProfileResponse.setLastname(user.getLastname());
            userProfileResponse.setEmail(user.getEmail());
            userProfileResponse.setPhone(user.getPhone());

            return ResponseHandler.responseBuilder("User Edited", HttpStatus.OK, userProfileResponse);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }


    public ResponseEntity<Object> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        // Retrieve the currently authenticated user's details from the security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (new BCryptPasswordEncoder().matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
                user.setPassword(new BCryptPasswordEncoder().encode(updatePasswordRequest.getNewPassword()));
                userRepository.save(user);
                return ResponseHandler.responseBuilder("Password Edited", HttpStatus.OK, new ArrayList<>());
            } else {
                return ResponseHandler.responseBuilder("Password mismatch error", HttpStatus.BAD_REQUEST, new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Password mismatch error", HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }
}


