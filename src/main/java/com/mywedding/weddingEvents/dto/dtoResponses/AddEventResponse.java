package com.mywedding.weddingEvents.dto.dtoResponses;

import com.mywedding.identity.entities.User;
import lombok.Data;

@Data
public class AddEventResponse {
    private Long id;
    private String name;
    private String location;
    private double price;
    private String description;
    private User createdBy;
}
