package com.mywedding.weddingHall.controller;


import com.mywedding.weddingHall.dto.dtoRequests.AddRatingAndCommentRequest;
import com.mywedding.weddingHall.dto.dtoRequests.AddWeddingHallRequest;
import com.mywedding.weddingHall.dto.dtoRequests.DeleteImageRequest;
import com.mywedding.weddingHall.dto.dtoRequests.UpdateWeddingHallRequest;
import com.mywedding.weddingHall.services.WeddingHallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class WeddingHallController {

    private final WeddingHallService weddingHallService;

    @PostMapping(path = "wedding-halls/create")
    public ResponseEntity<Object> createEvent (@RequestBody AddWeddingHallRequest addWeddingHallRequest) {
        // Call the updateUserProfile() method from AuthenticationService and return the result
        return weddingHallService.createWeddingHall(addWeddingHallRequest);
    }

    @PostMapping(path = "wedding-halls/upload-images/{weddingHallId}")
    public ResponseEntity<Object> uploadImage(@RequestParam("image")MultipartFile images [] ,@PathVariable Long weddingHallId) {
       return weddingHallService.uploadImage(images,weddingHallId);
    }
    @PostMapping(path = "wedding-halls/addRatingAndComment")
    public ResponseEntity<Object> addRatingAndComment(@RequestBody AddRatingAndCommentRequest addRatingAndCommentRequest) {
       return weddingHallService.addRatingAndComment(addRatingAndCommentRequest);
    }

    @GetMapping("wedding-halls/getImageUrl/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=weddingHallService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("wedding-halls/gethallinfo/{weddingHallId}")
    public ResponseEntity<Object> getWeddingHallByid(@PathVariable Long weddingHallId) {
        return weddingHallService.getWeddingHallById(weddingHallId);
    }
    @GetMapping("wedding-halls/getAll")
    public ResponseEntity<Object> getWeddingHalls() {
        return weddingHallService.getWeddingHalls();
    }

    @DeleteMapping("wedding-halls/delete-image")
    public ResponseEntity<Object> deleteWeddingHallImage(@RequestBody DeleteImageRequest deleteImageRequest) {
        return weddingHallService.deleteWeddingHallImage(deleteImageRequest);
    }
    @PutMapping("wedding-halls/update/{weddingHallId}")
    public ResponseEntity<Object> updateWeddingHall(@PathVariable Long weddingHallId,@RequestBody UpdateWeddingHallRequest updateWeddingHallRequest) {
        return weddingHallService.UpdateWeddingHall(weddingHallId,updateWeddingHallRequest);
    }
    @DeleteMapping("wedding-halls/delete/{weddingHallId}")
    public ResponseEntity<Object> deleteWeddingHall(@PathVariable Long weddingHallId) {
        return weddingHallService.DeleteWeddingHall(weddingHallId);
    }

}
