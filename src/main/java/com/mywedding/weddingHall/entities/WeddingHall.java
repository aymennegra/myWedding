package com.mywedding.weddingHall.entities;

import com.mywedding.identity.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "wedding_hall")
public class WeddingHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String adress;
    private double latitude;
    private double longitude;
    private int seats_number;
    private Double price;
    private String description;

    @ManyToOne
    @NotNull
    private User createdBy; // User who created the event

    @OneToMany(mappedBy = "weddingHall", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<WeddingHallImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "weddingHall", cascade = CascadeType.ALL)
    private List<UserReview> userReview = new ArrayList<>();

    // Getters and setters (generated by Lombok)
}
