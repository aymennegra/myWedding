package com.mywedding.weddingHall.dto.dtoResponses;

import com.mywedding.identity.entities.User;
import lombok.Data;

@Data
public class AddWeddingHallResponse {
    private Long id;
    private String name;
    private String adress;
    private double latitude;
    private double longitude;
    private int seats_number;
    private double price;
    private String description;
    private String createdBy;
}
