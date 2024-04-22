package com.mywedding.weddingPlanner.band.entity;

import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "band")
public class Band extends ServiceEntity {
    private double price;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "band_id") // Assuming you have a band_id column in the Artist table
    private List<Artist> artists = new ArrayList<>();
}