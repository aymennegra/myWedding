package com.mywedding.weddingPlanner.weddingHall.repositories;

import com.mywedding.weddingPlanner.weddingHall.entity.WeddingHall;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeddingHallRepository extends JpaRepository<WeddingHall,Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ServiceEntity w WHERE w.id = :weddingHallId")
    void deleteWeddingHall(Long weddingHallId);

    // Add a method to retrieve all wedding halls
    List<WeddingHall> findAll();
}
