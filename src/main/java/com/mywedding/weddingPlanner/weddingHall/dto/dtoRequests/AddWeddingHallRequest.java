package com.mywedding.weddingPlanner.weddingHall.dto.dtoRequests;


import com.mywedding.identity.entities.User;
import lombok.Data;

@Data
public class AddWeddingHallRequest {
    private String name;
    private String address;
    private String ville;
    private double latitude;
    private double longitude;
    private int seatsNumber;
    private double price;
    private String description;
    private User createdBy;
}
