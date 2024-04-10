package com.mywedding.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class DeleteImageRequest {
    private Long userId;
    private Long weddingHallId;
    private Long imageId;
}
