package com.mywedding.weddingHall.dto.dtoResponses;

import com.mywedding.weddingHall.entities.WeddingHallImage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateWeddingHallResponse {
    private String name;
    private String location;
    private Double price;
    private String description;
    private List<WeddingHallImage> images = new ArrayList<>();
}
