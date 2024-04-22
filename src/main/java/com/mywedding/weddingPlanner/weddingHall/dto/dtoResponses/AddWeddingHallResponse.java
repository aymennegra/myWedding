package com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses;

import lombok.Data;

@Data
public class AddWeddingHallResponse {
    private Long id;
    private String name;
    private String address;
    private String ville;
    private double latitude;
    private double longitude;
    private int seatsNumber;
    private double price;
    private String description;
    private String createdBy;
}
