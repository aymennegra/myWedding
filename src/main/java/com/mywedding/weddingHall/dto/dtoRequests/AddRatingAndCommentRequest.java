package com.mywedding.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class AddRatingAndCommentRequest {
    private long weddingHallId;
    private double rating;
    private String comment;
}
