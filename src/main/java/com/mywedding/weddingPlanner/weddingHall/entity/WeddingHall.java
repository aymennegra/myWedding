package com.mywedding.weddingPlanner.weddingHall.entity;


import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wedding_hall")
public class WeddingHall extends ServiceEntity {
    private int seatsNumber;
    private double price;
}
