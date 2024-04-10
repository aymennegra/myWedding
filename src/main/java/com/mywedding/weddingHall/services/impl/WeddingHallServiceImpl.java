package com.mywedding.weddingHall.services.impl;

import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingHall.dto.dtoResponses.AddWeddingHallResponse;
import com.mywedding.weddingHall.dto.dtoResponses.GetWeddingHallResponse;
import com.mywedding.weddingHall.entities.WeddingHall;
import com.mywedding.weddingHall.entities.WeddingHallImage;
import com.mywedding.weddingHall.repositories.WeddingHallImageRepository;
import com.mywedding.weddingHall.repositories.WeddingHallRepository;
import com.mywedding.weddingHall.services.WeddingHallService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeddingHallServiceImpl implements WeddingHallService {

    private final UserRepository userRepository;
    private final WeddingHallRepository weddingHallRepository;
    private final WeddingHallImageRepository weddingHallImageRepository;

    @Override
    public ResponseEntity<Object> createWeddingHall(AddWeddingHallRequest addWeddingHallRequest) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user.isAdmin()) {
                //save wedding event info
                WeddingHall weddingHall = new WeddingHall();
                weddingHall.setName(addWeddingHallRequest.getName());
                weddingHall.setLocation(addWeddingHallRequest.getLocation());
                weddingHall.setPrice(addWeddingHallRequest.getPrice());
                weddingHall.setDescription(addWeddingHallRequest.getDescription());
                weddingHall.setCreatedBy(user);
                weddingHallRepository.save(weddingHall);
                AddWeddingHallResponse addWeddingHallResponse = new AddWeddingHallResponse();
                addWeddingHallResponse.setId(weddingHall.getId());
                addWeddingHallResponse.setName(addWeddingHallRequest.getName());
                addWeddingHallResponse.setLocation(addWeddingHallRequest.getLocation());
                addWeddingHallResponse.setPrice(addWeddingHallRequest.getPrice());
                addWeddingHallResponse.setDescription(addWeddingHallRequest.getDescription());
                addWeddingHallResponse.setCreatedBy(user);

                return ResponseHandler.responseBuilder("Event created", HttpStatus.OK,
                        addWeddingHallResponse);
            } else {
                return ResponseHandler.responseBuilder("sorry, you are not allowed to create events..", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Unauthorized", HttpStatus.UNAUTHORIZED, new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<Object> processUploadedFiles(MultipartFile[] files,Long weddingHallId) {
        try {
            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            // Process each uploaded file
            for (MultipartFile file : files) {
                WeddingHallImage weddingHallImage = new WeddingHallImage();
                String fileName = "image_" + System.currentTimeMillis() + ".jpg";
                String filePath = "src/img/" + fileName;
                saveImageToFile(file, filePath);
                weddingHallImage.setImagePath(filePath);
                weddingHallImage.setWeddingHall(weddingHall);
                weddingHall.getImages().add(weddingHallImage);
            }

            // Save the wedding hall with images
            weddingHallRepository.save(weddingHall);

            return ResponseHandler.responseBuilder("Perfect, images uploaded successfully!", HttpStatus.OK, new ArrayList<>());
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder("Wedding hall not found", HttpStatus.NOT_FOUND, new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImageToFile(MultipartFile file, String filePath) throws IOException {
        // Create the directory if it doesn't exist
        File directory = new File("src/img");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to the specified path
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());
        System.out.println("Image saved successfully at: " + filePath);
    }

    @Override
    public ResponseEntity<Object> getWeddingHallWithImages(Long weddingHallId) {
        try {
            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            // Retrieve images associated with the wedding hall
            List<WeddingHallImage> images = weddingHallImageRepository.findByWeddingHall(weddingHall);

            // Prepare the response object
            GetWeddingHallResponse response = new GetWeddingHallResponse();
            response.setId(weddingHall.getId());
            response.setName(weddingHall.getName());
            response.setLocation(weddingHall.getLocation());
            response.setPrice(weddingHall.getPrice());
            response.setDescription(weddingHall.getDescription());
            // Populate the list of image URLs
            List<String> imageUrls = new ArrayList<>();
            for (WeddingHallImage image : images) {
                imageUrls.add(image.getImagePath()); // Assuming you have a method getImagePath() to get the path of the image
            }

            // Update the response object with image URLs
            response.setImageUrls(imageUrls); // Assuming you have setter for images in GetWeddingHallResponse


            return ResponseHandler.responseBuilder("Retrieved wedding hall with images", HttpStatus.OK, response);
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder("Wedding hall not found", HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to retrieve wedding hall with images", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
