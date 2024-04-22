package com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses;

import lombok.Data;

@Data
public class AddRatingAndCommentResponse {
    private double rating;
    private String comment;
    private String username;
    private String time;
}
