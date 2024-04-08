package com.mywedding.identity.services;

import com.mywedding.identity.dto.dtoRequests.RefreshTokenRequest;
import com.mywedding.identity.dto.dtoRequests.SignInRequest;
import com.mywedding.identity.dto.dtoRequests.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<Object> signup (SignUpRequest signUpRequest);
    ResponseEntity<Object> signin (SignInRequest signInRequest);
    ResponseEntity<Object> refreshToken (RefreshTokenRequest refreshTokenRequest);
}
