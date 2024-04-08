package com.mywedding.weddingEvents.dto.dtoRequests;


import com.mywedding.identity.entities.User;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class AddEventRequest {
    private String name;
    private String location;
    private double price;
    private String description;
    private User createdBy;
}
