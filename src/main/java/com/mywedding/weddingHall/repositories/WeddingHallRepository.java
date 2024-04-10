package com.mywedding.weddingHall.repositories;

import com.mywedding.weddingHall.entities.WeddingHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeddingHallRepository extends JpaRepository<WeddingHall,Long> {
    // Add a method to retrieve all wedding halls
    List<WeddingHall> findAll();
}
