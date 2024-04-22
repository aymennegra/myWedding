package com.mywedding.weddingPlanner.photographer.entity;

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
@Table(name = "photographer")
public class Photographer extends ServiceEntity {
    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotographyPack> photographyPacks = new ArrayList<>();
}
