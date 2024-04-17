package com.mywedding.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class UpdateWeddingHallRequest {
    private String name;
    private String adress;
    private Double latitude;
    private Double longitude;
    private Double price;
    private String description;
}
