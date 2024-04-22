package com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class DeleteImageRequest {
    private Long weddingHallId;
    private Long imageId;
}
