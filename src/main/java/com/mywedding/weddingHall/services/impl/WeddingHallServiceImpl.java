package com.mywedding.weddingHall.services.impl;

import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingHall.dto.dtoRequests.DeleteImageRequest;
import com.mywedding.weddingHall.dto.dtoResponses.AddWeddingHallResponse;
import com.mywedding.weddingHall.dto.dtoResponses.GetWeddingHallResponse;
import com.mywedding.weddingHall.entities.WeddingHall;
import com.mywedding.weddingHall.entities.WeddingHallImage;
import com.mywedding.weddingHall.repositories.WeddingHallImageRepository;
import com.mywedding.weddingHall.repositories.WeddingHallRepository;
import com.mywedding.weddingHall.services.WeddingHallService;
import com.mywedding.weddingHall.utils.ImageUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<Object> uploadImage(MultipartFile[] uploadedImages, Long weddingHallId) {
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();

        // Format the timestamp to include milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssSSS");
        String timestamp = now.format(formatter);
        // Retrieve the wedding hall
        WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (MultipartFile uploadedImage : uploadedImages) {
            try {
                WeddingHallImage savedImage = weddingHallImageRepository.save(WeddingHallImage.builder()
                        .name(timestamp + uploadedImage.getOriginalFilename())
                        .type(uploadedImage.getContentType())
                        .imageData(ImageUtils.compressImage(uploadedImage.getBytes()))
                        .weddingHall(weddingHall).build());

                if (savedImage != null) {
                    successMessages.add("File uploaded successfully: " + uploadedImage.getOriginalFilename());
                }
            } catch (Exception e) {
                // Handle any exceptions, for example, log the error
                e.printStackTrace();
                errorMessages.add("Failed to upload file: " + uploadedImage.getOriginalFilename());
            }
        }

        if (!errorMessages.isEmpty()) {
            // If there were any errors, return a bad request status with error messages
            return ResponseHandler.responseBuilder("Failed to upload one or more files", HttpStatus.BAD_REQUEST, errorMessages);
        } else {
            // If all files were uploaded successfully, return an OK status with success messages
            return ResponseHandler.responseBuilder("All files uploaded successfully", HttpStatus.OK, successMessages);
        }
    }
    @Override
    public byte[] downloadImage(String fileName){
        Optional<WeddingHallImage> dbImageData = weddingHallImageRepository.findByName(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

    @Override
    public ResponseEntity<Object> getWeddingHallById(Long weddingHallId) {
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
                imageUrls.add("http://localhost:8080/api/v1/admin/wedding-halls/getImageUrl/"+image.getName()); // Assuming you have a method getImagePath() to get the path of the image
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

    public ResponseEntity<Object> getWeddingHalls(){
        try {
            // Retrieve all wedding halls
            List<WeddingHall> weddingHalls = weddingHallRepository.findAll();

            // Prepare a list to hold wedding halls with images
            List<GetWeddingHallResponse> weddingHallResponses = new ArrayList<>();

            // Iterate over each wedding hall to retrieve its associated images
            for (WeddingHall weddingHall : weddingHalls) {
                // Retrieve images associated with the wedding hall
                List<WeddingHallImage> images = weddingHallImageRepository.findByWeddingHall(weddingHall);

                // Prepare the response object for the current wedding hall
                GetWeddingHallResponse response = new GetWeddingHallResponse();
                response.setId(weddingHall.getId());
                response.setName(weddingHall.getName());
                response.setLocation(weddingHall.getLocation());
                response.setPrice(weddingHall.getPrice());
                response.setDescription(weddingHall.getDescription());

                // Populate the list of clickable image URLs for the current wedding hall
                List<String> imageUrls = new ArrayList<>();
                for (WeddingHallImage image : images) {
                    // Assuming you have a method getImagePath() to get the path of the image
                    String imageUrl = "http://localhost:8080/api/v1/admin/wedding-halls/getImageUrl/" + image.getName(); // Update the base URL accordingly
                    imageUrls.add(imageUrl);
                }

                // Update the response object with image URLs
                response.setImageUrls(imageUrls); // Assuming you have setter for images in GetWeddingHallResponse

                // Add the response object for the current wedding hall to the list
                weddingHallResponses.add(response);
            }

            return ResponseHandler.responseBuilder("Retrieved all wedding halls with images", HttpStatus.OK, weddingHallResponses);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to retrieve wedding halls with images", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Override
    public ResponseEntity<Object> deleteWeddingHallImage(DeleteImageRequest deleteImageRequest) {
        try {
            // Retrieve the authenticated user
            User authenticatedUser = userRepository.findById(deleteImageRequest.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(deleteImageRequest.getWeddingHallId())
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            // Ensure that the authenticated user is the creator of the wedding hall
            if (!authenticatedUser.equals(weddingHall.getCreatedBy())) {
                return ResponseHandler.responseBuilder("You are not authorized to delete images for this wedding hall", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }

            // Retrieve the image associated with the wedding hall
            WeddingHallImage image = weddingHallImageRepository.findById(deleteImageRequest.getImageId())
                    .orElseThrow(() -> new EntityNotFoundException("Image not found"));

            // Ensure the image is associated with the correct wedding hall
            if (!weddingHall.equals(image.getWeddingHall())) {
                return ResponseHandler.responseBuilder("Image does not belong to the specified wedding hall", HttpStatus.NOT_FOUND, new ArrayList<>());
            }

            // Delete the image
            weddingHallImageRepository.deleteWeddingHallImageById(deleteImageRequest.getImageId());
            weddingHallImageRepository.flush();

            return ResponseHandler.responseBuilder("Image deleted successfully", HttpStatus.OK, null);
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
