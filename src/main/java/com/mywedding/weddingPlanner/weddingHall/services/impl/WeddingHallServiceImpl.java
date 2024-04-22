package com.mywedding.weddingPlanner.weddingHall.services.impl;

import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.entities.UserType;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceImages;
import com.mywedding.weddingPlanner.plannerBaseEntities.UserReview;
import com.mywedding.weddingPlanner.utils.ImageUtils;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.AddRatingAndCommentRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.DeleteImageRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.UpdateWeddingHallRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses.AddRatingAndCommentResponse;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses.AddWeddingHallResponse;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses.GetWeddingHallResponse;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses.UpdateWeddingHallResponse;
import com.mywedding.weddingPlanner.weddingHall.entity.WeddingHall;
import com.mywedding.weddingPlanner.weddingHall.repositories.RatingCommentRepository;
import com.mywedding.weddingPlanner.weddingHall.repositories.WeddingHallImageRepository;
import com.mywedding.weddingPlanner.weddingHall.repositories.WeddingHallRepository;
import com.mywedding.weddingPlanner.weddingHall.services.WeddingHallService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeddingHallServiceImpl implements WeddingHallService {

    private final UserRepository userRepository;
    private final WeddingHallRepository weddingHallRepository;
    private final WeddingHallImageRepository weddingHallImageRepository;
    private final RatingCommentRepository ratingCommentRepository;

    @Override
    public ResponseEntity<Object> createWeddingHall(AddWeddingHallRequest addWeddingHallRequest) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (user.isAdmin() || user.getUserType().equals(UserType.WEDDING_HALL_OWNER)) {
                WeddingHall weddingHall = getWeddingHall(addWeddingHallRequest, user);

                weddingHallRepository.save(weddingHall);

                AddWeddingHallResponse addWeddingHallResponse = getAddWeddingHallResponse(addWeddingHallRequest, weddingHall, user);

                return ResponseHandler.responseBuilder("Event created", HttpStatus.OK, addWeddingHallResponse);
            } else {
                return ResponseHandler.responseBuilder("Sorry, you are not allowed to create wedding halls.", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }
        } catch (UsernameNotFoundException e) {
            return ResponseHandler.responseBuilder("User not found", HttpStatus.UNAUTHORIZED, new ArrayList<>());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Unauthorized", HttpStatus.UNAUTHORIZED, new ArrayList<>());
        }
    }

    private static WeddingHall getWeddingHall(AddWeddingHallRequest addWeddingHallRequest, User user) {
        WeddingHall weddingHall = new WeddingHall();
        weddingHall.setName(addWeddingHallRequest.getName());
        weddingHall.setAddress(addWeddingHallRequest.getAddress());
        weddingHall.setVille(addWeddingHallRequest.getVille());
        weddingHall.setLatitude(addWeddingHallRequest.getLatitude());
        weddingHall.setLongitude(addWeddingHallRequest.getLongitude());
        weddingHall.setSeatsNumber(addWeddingHallRequest.getSeatsNumber());
        weddingHall.setPrice(addWeddingHallRequest.getPrice());
        weddingHall.setDescription(addWeddingHallRequest.getDescription());
        weddingHall.setCreatedBy(user);
        return weddingHall;
    }


    private static AddWeddingHallResponse getAddWeddingHallResponse(AddWeddingHallRequest addWeddingHallRequest, WeddingHall weddingHall, User user) {
        AddWeddingHallResponse addWeddingHallResponse = new AddWeddingHallResponse();
        addWeddingHallResponse.setId(weddingHall.getId());
        addWeddingHallResponse.setName(addWeddingHallRequest.getName());
        addWeddingHallResponse.setVille(addWeddingHallRequest.getVille());
        addWeddingHallResponse.setAddress(addWeddingHallRequest.getAddress());
        addWeddingHallResponse.setLatitude(addWeddingHallRequest.getLatitude());
        addWeddingHallResponse.setLongitude(addWeddingHallRequest.getLongitude());
        addWeddingHallResponse.setSeatsNumber(addWeddingHallRequest.getSeatsNumber());
        addWeddingHallResponse.setPrice(addWeddingHallRequest.getPrice());
        addWeddingHallResponse.setDescription(addWeddingHallRequest.getDescription());
        addWeddingHallResponse.setCreatedBy(user.getFirstname());
        return addWeddingHallResponse;
    }

    @Override
    public ResponseEntity<Object> uploadImage(MultipartFile[] uploadedImages, Long weddingHallId) {
        // Get the current timestamp
        LocalDateTime now = LocalDateTime.now();
        // Format the timestamp to include milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssSSS");
        String timestamp = now.format(formatter);

        // Retrieve the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the user exists in the database based on the email (assuming email is the username)
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retrieve the wedding hall
        WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
            if (user.equals(weddingHall.getCreatedBy())||user.isAdmin()){
                for (MultipartFile uploadedImage : uploadedImages) {
                    try {
                        weddingHallImageRepository.save(ServiceImages.builder()
                                .name(timestamp + uploadedImage.getOriginalFilename())
                                .type(uploadedImage.getContentType())
                                .imageData(ImageUtils.compressImage(uploadedImage.getBytes()))
                                .weddingHall(weddingHall).build());

                        successMessages.add("File uploaded successfully: " +"http://localhost:8080/api/v1/wedding-halls/getImageUrl/" + timestamp + uploadedImage.getOriginalFilename());
                    } catch (Exception e) {
                        // Handle any exceptions, for example, log the error
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
            }else {
                return ResponseHandler.responseBuilder("sorry, you are not allowed to upload images to this wedding hall.. ", HttpStatus.FORBIDDEN, successMessages);
            }
    }

    @Override
    public byte[] downloadImage(String fileName) {
        Optional<ServiceImages> dbImageDataOptional = weddingHallImageRepository.findByName(fileName);
        if (dbImageDataOptional.isPresent()) {
            ServiceImages dbImageData = dbImageDataOptional.get();
            return ImageUtils.decompressImage(dbImageData.getImageData());
        } else {
            // Handle the case where the image with the given filename is not found
            // For example, you could throw an exception or return a default image
            return null; // Or throw an exception, depending on your use case
        }
    }

    @Override
    public ResponseEntity<Object> getWeddingHallById(Long weddingHallId) {
        try {
            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            // Retrieve images associated with the wedding hall
            List<ServiceImages> images = weddingHallImageRepository.findByWeddingHall(weddingHall);
            // Retrieve reviews associated with the wedding hall
            List<UserReview> reviews = ratingCommentRepository.findByWeddingHall(weddingHall);

            // Calculate average rating
            double averageRating = calculateAverageRating(reviews);

            // Prepare the response object
            GetWeddingHallResponse response = getWeddingHallResponse(weddingHall, images, reviews, averageRating);

            return ResponseHandler.responseBuilder("Retrieved wedding hall with images and reviews", HttpStatus.OK, response);
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder("Wedding hall not found", HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to retrieve wedding hall with images and reviews", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    public ResponseEntity<Object> getWeddingHalls() {
        try {
            // Retrieve all wedding halls
            List<WeddingHall> weddingHalls = weddingHallRepository.findAll();

            // Prepare a list to hold wedding hall responses
            List<GetWeddingHallResponse> weddingHallResponses = new ArrayList<>();

            // Iterate over each wedding hall to retrieve its associated images, reviews, and calculate average rating
            for (WeddingHall weddingHall : weddingHalls) {
                List<ServiceImages> images = weddingHall.getImages();
                List<UserReview> reviews = weddingHall.getUserReview();
                double averageRating = calculateAverageRating(reviews);
                GetWeddingHallResponse response = getWeddingHallResponse(weddingHall, images, reviews, averageRating);
                weddingHallResponses.add(response);
            }

            return ResponseHandler.responseBuilder("Retrieved all wedding halls with images, reviews, and average ratings", HttpStatus.OK, weddingHallResponses);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to retrieve wedding halls with images, reviews, and average ratings", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    private static GetWeddingHallResponse getWeddingHallResponse(WeddingHall weddingHall, List<ServiceImages> images, List<UserReview> reviews, double averageRating) {
        GetWeddingHallResponse response = new GetWeddingHallResponse();
        response.setId(weddingHall.getId());
        response.setName(weddingHall.getName());
        response.setAdress(weddingHall.getAddress());
        response.setLatitude(weddingHall.getLatitude());
        response.setLongitude(weddingHall.getLongitude());
        response.setPrice(weddingHall.getPrice());
        response.setSeats_number(weddingHall.getSeatsNumber());
        response.setDescription(weddingHall.getDescription());
        response.setCreatedBy(weddingHall.getCreatedBy().getFirstname() + " " + weddingHall.getCreatedBy().getLastname());

        // Populate the list of clickable image URLs for the current wedding hall
        List<String> imageUrls = new ArrayList<>();
        for (ServiceImages image : images) {
            // Assuming you have a method getImagePath() to get the path of the image
            String imageUrl = "http://localhost:8080/api/v1/wedding-halls/getImageUrl/" + image.getName(); // Update the base URL accordingly
            imageUrls.add(imageUrl);
        }

        // Prepare the list of user review responses
        List<AddRatingAndCommentResponse> userReviewResponses = getAddRatingAndCommentResponses(reviews);
        response.setUsersReview(userReviewResponses);

        // Update the response object with image URLs
        response.setImageUrls(imageUrls); // Assuming you have setter for images in GetWeddingHallResponse

        // Set average rating
        DecimalFormat formatRating = new DecimalFormat("#.#");

        response.setAverageRating(Double.parseDouble(formatRating.format(averageRating)));

        return response;
    }

    private static List<AddRatingAndCommentResponse> getAddRatingAndCommentResponses(List<UserReview> reviews) {
        List<AddRatingAndCommentResponse> userReviewResponses = new ArrayList<>();
        for (UserReview review : reviews) {
            AddRatingAndCommentResponse userReviewResponse = new AddRatingAndCommentResponse();
            userReviewResponse.setRating(review.getRating());
            userReviewResponse.setComment(review.getComment());
            userReviewResponse.setTime(review.getTime());
            // Optionally, you can include other user-related information here
            userReviewResponse.setUsername(review.getUser().getFirstname() + " " + review.getUser().getLastname());
            userReviewResponses.add(userReviewResponse);
        }
        return userReviewResponses;
    }

    @Override
    public ResponseEntity<Object> deleteWeddingHallImage(DeleteImageRequest deleteImageRequest) {
        try {
            // Retrieve the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(deleteImageRequest.getWeddingHallId())
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            // Ensure that the authenticated user is the creator of the wedding hall or is an admin
            if (user.isAdmin() || user.equals(weddingHall.getCreatedBy())) {
                // Retrieve the image associated with the wedding hall
                Optional<ServiceImages> optionalImage = weddingHallImageRepository.findById(deleteImageRequest.getImageId());
                if (optionalImage.isPresent()) {
                    ServiceImages image = optionalImage.get();
                    // Ensure the image is associated with the correct wedding hall
                    if (!weddingHall.equals(image.getWeddingHall())) {
                        return ResponseHandler.responseBuilder("Image does not belong to the specified wedding hall", HttpStatus.NOT_FOUND, new ArrayList<>());
                    }
                    // Delete the image
                    weddingHallImageRepository.delete(image);
                    return ResponseHandler.responseBuilder("Image deleted successfully", HttpStatus.OK, new ArrayList<>());
                } else {
                    return ResponseHandler.responseBuilder("Image not found", HttpStatus.NOT_FOUND, new ArrayList<>());
                }
            } else {
                return ResponseHandler.responseBuilder("You are not authorized to delete images for this wedding hall", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND, new ArrayList<>());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @Override
    public ResponseEntity<Object> UpdateWeddingHall(Long weddingHallId, UpdateWeddingHallRequest updateWeddingHallRequest) {

        try {
            // Retrieve the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            if(weddingHall.getCreatedBy().equals(user)||user.isAdmin())
            {
                if (updateWeddingHallRequest.getName() == null) {
                    weddingHall.setName(weddingHall.getName());
                }else {
                    weddingHall.setName(updateWeddingHallRequest.getName());
                }
                if (updateWeddingHallRequest.getSeatsNumber()== 0) {
                    // If the seats_number in the update request is null, keep the existing value
                    weddingHall.setSeatsNumber(weddingHall.getSeatsNumber());
                } else {
                    // If the seats_number in the update request is not null, update the value
                    weddingHall.setSeatsNumber(updateWeddingHallRequest.getSeatsNumber());
                }
                if (updateWeddingHallRequest.getVille()== null) {
                    // If the seats_number in the update request is null, keep the existing value
                    weddingHall.setVille(weddingHall.getVille());
                } else {
                    // If the seats_number in the update request is not null, update the value
                    weddingHall.setVille(updateWeddingHallRequest.getVille());
                }

                if (updateWeddingHallRequest.getAddress() == null) {
                    weddingHall.setAddress(weddingHall.getAddress());
                }else {
                    weddingHall.setAddress(updateWeddingHallRequest.getAddress());
                }
                if (updateWeddingHallRequest.getLatitude() == null) {
                    weddingHall.setLatitude(weddingHall.getLatitude());
                }else {
                    weddingHall.setLatitude(updateWeddingHallRequest.getLatitude());
                }
                if (updateWeddingHallRequest.getLongitude() == null) {
                    weddingHall.setLongitude(weddingHall.getLongitude());
                }else {
                    weddingHall.setLongitude(updateWeddingHallRequest.getLongitude());
                }
                if (updateWeddingHallRequest.getPrice() == null) {
                    weddingHall.setPrice(weddingHall.getPrice());
                }else {
                    weddingHall.setPrice(updateWeddingHallRequest.getPrice());
                }
                if (updateWeddingHallRequest.getDescription() == null) {
                    weddingHall.setDescription(weddingHall.getDescription());
                }else {
                    weddingHall.setDescription(updateWeddingHallRequest.getDescription());
                }
                weddingHallRepository.save(weddingHall);
                UpdateWeddingHallResponse updateWeddingHallResponse = new UpdateWeddingHallResponse();
                updateWeddingHallResponse.setName(weddingHall.getName());
                updateWeddingHallResponse.setAddress(weddingHall.getAddress());
                updateWeddingHallResponse.setVille(weddingHall.getVille());
                updateWeddingHallResponse.setPrice(weddingHall.getPrice());
                updateWeddingHallResponse.setSeatsNumber(weddingHall.getSeatsNumber());
                updateWeddingHallResponse.setDescription(weddingHall.getDescription());
                return ResponseHandler.responseBuilder("Wedding hall Edited successfully", HttpStatus.OK,
                        updateWeddingHallResponse);

            }else {
                return ResponseHandler.responseBuilder("sorry, you are not allowed to upload images to this wedding hall.. ", HttpStatus.FORBIDDEN, new ArrayList<>());
            }
        }catch (Exception e){
            return ResponseHandler.responseBuilder("updated failed ", HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }

    }

    @Override
    public ResponseEntity<Object> deleteWeddingHall(Long weddingHallId) {
        try {
            // Retrieve the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Retrieve the wedding hall
            WeddingHall weddingHall = weddingHallRepository.findById(weddingHallId)
                    .orElseThrow(() -> new EntityNotFoundException("Wedding hall not found"));

            if (weddingHall.getCreatedBy().equals(user)) {
                weddingHallRepository.deleteById(weddingHallId);
                return ResponseHandler.responseBuilder("Wedding hall deleted successfully", HttpStatus.OK, new ArrayList<>());
            } else {
                return ResponseHandler.responseBuilder("Failed to delete wedding hall. User does not have permission", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder("Wedding hall not found", HttpStatus.NOT_FOUND, new ArrayList<>());
        } catch (UsernameNotFoundException e) {
            return ResponseHandler.responseBuilder("User not found", HttpStatus.NOT_FOUND, new ArrayList<>());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to delete wedding hall", HttpStatus.INTERNAL_SERVER_ERROR, new ArrayList<>());
        }
    }


    public ResponseEntity<Object> addRatingAndComment(AddRatingAndCommentRequest addRatingAndCommentRequest) {
        try {
            LocalDateTime now = LocalDateTime.now();
            // Format the timestamp to include milliseconds
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssSSS");
            String timestamp = now.format(formatter);
            // Retrieve the authenticated user
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Retrieve the wedding hall
            Optional<WeddingHall> optionalWeddingHall = weddingHallRepository.findById(addRatingAndCommentRequest.getWeddingHallId());
            if (optionalWeddingHall.isEmpty()) {
                throw new EntityNotFoundException("Wedding hall not found");
            }
            WeddingHall weddingHall = optionalWeddingHall.get();

            // Create and save the rating and comment
            UserReview ratingComment = new UserReview();
            ratingComment.setRating(addRatingAndCommentRequest.getRating());
            ratingComment.setComment(addRatingAndCommentRequest.getComment());
            ratingComment.setTime(timestamp);
            ratingComment.setUser(user);
            ratingComment.setWeddingHall(weddingHall);

            ratingCommentRepository.save(ratingComment);

            // Prepare the response
            AddRatingAndCommentResponse addRatingAndCommentResponse = new AddRatingAndCommentResponse();
            addRatingAndCommentResponse.setUsername(user.getFirstname() + " " + user.getLastname());
            addRatingAndCommentResponse.setRating(addRatingAndCommentRequest.getRating());
            addRatingAndCommentResponse.setComment(addRatingAndCommentRequest.getComment());
            addRatingAndCommentResponse.setTime(timestamp);

            return ResponseHandler.responseBuilder("Thank you for your feedback :)", HttpStatus.OK, addRatingAndCommentResponse);
        } catch (EntityNotFoundException e) {
            return ResponseHandler.responseBuilder("Wedding hall not found", HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Failed to add rating", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    private double calculateAverageRating(List<UserReview> reviews) {
        if (reviews.isEmpty()) {
            return 0.0; // Return 0 if no reviews are available
        }
        // Calculate the sum of all ratings
        double sumOfRatings = reviews.stream().mapToDouble(UserReview::getRating).sum();
        // Calculate the average rating
        return sumOfRatings / reviews.size();
    }

}
