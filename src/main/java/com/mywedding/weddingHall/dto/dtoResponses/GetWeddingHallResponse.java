package com.mywedding.weddingHall.dto.dtoResponses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetWeddingHallResponse {

    private Long id;
    private String name;
    private String adress;
    private double latitude;
    private double longitude;
    private int seats_number;
    private double averageRating;
    private double price;
    private String description;
    private String createdBy;
    private List<String> imageUrls = new ArrayList<>();
    private List<AddRatingAndCommentResponse> usersReview = new ArrayList<>();
}
