package com.mywedding.weddingHall.repositories;

import com.mywedding.weddingHall.entities.WeddingHall;
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
    @Query("DELETE FROM WeddingHall w WHERE w.id = :weddingHallId")
    void deleteWeddingHall(Long weddingHallId);

    // Add a method to retrieve all wedding halls
    List<WeddingHall> findAll();
}
