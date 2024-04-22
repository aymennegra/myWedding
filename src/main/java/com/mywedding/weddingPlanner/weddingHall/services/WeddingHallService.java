package com.mywedding.weddingPlanner.weddingHall.services;

import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.AddRatingAndCommentRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.DeleteImageRequest;
import com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests.UpdateWeddingHallRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface WeddingHallService {

    ResponseEntity<Object> createWeddingHall(AddWeddingHallRequest addWeddingHallRequest);
    ResponseEntity<Object> getWeddingHallById(Long weddingHallId);
    ResponseEntity<Object> getWeddingHalls();
    ResponseEntity<Object> uploadImage(MultipartFile[] uploadedImages,Long weddingHallId);
    byte[] downloadImage(String fileName);
    ResponseEntity<Object> deleteWeddingHallImage(DeleteImageRequest deleteImageRequest);
    ResponseEntity<Object> UpdateWeddingHall(Long weddingHallId, UpdateWeddingHallRequest updateWeddingHallRequest);
    ResponseEntity<Object> deleteWeddingHall(Long weddingHallId);
    ResponseEntity<Object> addRatingAndComment(AddRatingAndCommentRequest addRatingAndCommentRequest);
}
