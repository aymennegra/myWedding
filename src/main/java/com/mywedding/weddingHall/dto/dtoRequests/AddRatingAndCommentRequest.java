package com.mywedding.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class AddRatingAndCommentRequest {
    private long weddingHallId;
    private int rating;
    private String comment;
}
