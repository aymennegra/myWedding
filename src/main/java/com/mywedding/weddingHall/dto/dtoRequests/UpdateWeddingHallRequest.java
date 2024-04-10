package com.mywedding.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class UpdateWeddingHallRequest {
    private String name;
    private String location;
    private Double price;
    private String description;
}
