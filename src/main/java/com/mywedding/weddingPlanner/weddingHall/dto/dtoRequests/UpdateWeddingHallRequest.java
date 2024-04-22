package com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests;

import lombok.Data;

@Data
public class UpdateWeddingHallRequest {
    private String name;
    private String address;
    private String ville;
    private int seatsNumber;
    private Double latitude;
    private Double longitude;
    private Double price;
    private String description;
}
