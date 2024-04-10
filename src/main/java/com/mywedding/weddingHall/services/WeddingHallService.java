package com.mywedding.weddingHall.services;

import com.mywedding.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface WeddingHallService {

    ResponseEntity<Object> createWeddingHall(AddWeddingHallRequest addWeddingHallRequest);
    ResponseEntity<Object> processUploadedFiles(MultipartFile[] files,Long weddingHallId);
    ResponseEntity<Object> getWeddingHallWithImages(Long weddingHallId);
}
