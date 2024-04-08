package com.mywedding;

import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.dto.dtoResponses.SignUpResponse;
import com.mywedding.identity.entities.RefreshToken;
import com.mywedding.identity.entities.Role;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.RefreshTokenRepository;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.identity.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@SpringBootApplication
public class IdentityApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(IdentityApplication.class, args);
	}

	@Override
	public void run(String... args) {
		adminsignup();
	}

	public ResponseEntity<Object> adminsignup() {
		String refreshToken;
		String extractedRefreshtokenId;
		Optional<User> existingUserOptional = userRepository.findByEmail("admin@gmail.com");
		// Check if a user with the provided phone number already exists
		Optional<User> existingUserByPhone = userRepository.findByPhone("0000");
		if (existingUserOptional.isPresent()) {
			// User with the provided email already exists
			return ResponseHandler.responseBuilder("User with email " + "admin@gmail.com" + " already exists", HttpStatus.UNAUTHORIZED, new ArrayList<>());
		} else if (existingUserByPhone.isPresent()) {
			// User with the provided phone already exists
			return ResponseHandler.responseBuilder("User with phone number " + "0000" + " already exists", HttpStatus.UNAUTHORIZED, new ArrayList<>());
		} else {//ResponseHa
			try {
				//create admin account
				User user = new User();
				refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
				extractedRefreshtokenId = jwtService.extractRefreshTokenId(refreshToken);
				user.setEmail("admin@gmail.com");
				user.setFirstname("admin firstname");
				user.setLastname("admin lasname");
				user.setPhone("0000");
				user.setRole(Role.ADMIN);
				user.setRefreshToken(extractedRefreshtokenId);
				user.setPassword(passwordEncoder.encode("admin"));
				userRepository.save(user);
				//update refresh token table
				RefreshToken refreshTokenEntity = new RefreshToken();
				refreshTokenEntity.setToken(extractedRefreshtokenId);
				refreshTokenEntity.setUser(user);
				refreshTokenEntity.setExpirationDate(new Date(System.currentTimeMillis() + 100L * 365 * 24 * 3600 * 1000));
				refreshTokenRepository.save(refreshTokenEntity);
				//get response
				var jwt = jwtService.generateToken(user);
				SignUpResponse signUpResponse = new SignUpResponse();
				signUpResponse.setFirstname("admin firstname");
				signUpResponse.setLastname("admin lastname");
				signUpResponse.setEmail("admin@gmail.com");
				signUpResponse.setPhone("0000");
				signUpResponse.setRefreshToken(user.getRefreshToken());
				signUpResponse.setAccessToken(jwt);
				return ResponseHandler.responseBuilder("user created", HttpStatus.OK,
						signUpResponse);
			} catch (Exception e) {
				return ResponseHandler.responseBuilder("Unauthorized", HttpStatus.UNAUTHORIZED, new ArrayList<>());
			}
		}
	}
}
