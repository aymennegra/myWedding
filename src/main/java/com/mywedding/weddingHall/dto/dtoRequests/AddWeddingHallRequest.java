package com.mywedding.weddingHall.dto.dtoRequests;


import com.mywedding.identity.entities.User;
import lombok.Data;

@Data
public class AddWeddingHallRequest {
    private String name;
    private String adress;
    private double latitude;
    private double longitude;
    private int seats_number;
    private double price;
    private String description;
    private User createdBy;
}
