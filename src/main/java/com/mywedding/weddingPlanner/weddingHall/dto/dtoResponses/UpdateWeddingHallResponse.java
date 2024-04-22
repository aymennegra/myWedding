package com.mywedding.weddingPlanner.weddingHall.dto.dtoResponses;

import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceImages;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateWeddingHallResponse {
    private String name;
    private String address;
    private String ville;
    private double latitude;
    private double longitude;
    private int seatsNumber;
    private Double price;
    private String description;
    private List<ServiceImages> images = new ArrayList<>();
}
