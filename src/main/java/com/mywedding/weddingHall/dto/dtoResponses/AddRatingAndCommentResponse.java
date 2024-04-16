package com.mywedding.weddingHall.dto.dtoResponses;

import lombok.Data;

@Data
public class AddRatingAndCommentResponse {
    private int rating;
    private String comment;
    private String username;
}
