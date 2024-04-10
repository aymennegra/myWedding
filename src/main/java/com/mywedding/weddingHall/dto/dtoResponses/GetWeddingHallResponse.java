package com.mywedding.weddingHall.dto.dtoResponses;

import com.mywedding.weddingHall.entities.WeddingHallImage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetWeddingHallResponse {

    private Long id;
    private String name;
    private String location;
    private double price;
    private String description;
    private List<String> imageUrls = new ArrayList<>();
}
