package com.mywedding.weddingHall.controller;


import com.mywedding.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingHall.services.WeddingHallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class WeddingHallController {

    private final WeddingHallService weddingHallService;

    @PostMapping(path = "admin/wedding-halls/create")
    public ResponseEntity<Object> createEvent (@RequestBody AddWeddingHallRequest addWeddingHallRequest) {
        // Call the updateUserProfile() method from AuthenticationService and return the result
        return weddingHallService.createWeddingHall(addWeddingHallRequest);
    }

    @PostMapping(path = "admin/wedding-halls/images/{weddingHallId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadImages(@PathVariable Long weddingHallId, @RequestPart(value = "files") MultipartFile[] files) {
       return weddingHallService.processUploadedFiles( files,weddingHallId);
    }

    @GetMapping("admin/wedding-halls/gethallinfo/{weddingHallId}")
    public ResponseEntity<Object> getWeddingHallsWithImages(@PathVariable Long weddingHallId) {
        return weddingHallService.getWeddingHallWithImages(weddingHallId);
    }

}
