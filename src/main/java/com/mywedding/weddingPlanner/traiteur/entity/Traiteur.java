package com.mywedding.weddingPlanner.traiteur.entity;

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
@Table(name = "traiteur")
public class Traiteur extends ServiceEntity {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "traiteur_id")
    private List<Food> food = new ArrayList<>();
}
