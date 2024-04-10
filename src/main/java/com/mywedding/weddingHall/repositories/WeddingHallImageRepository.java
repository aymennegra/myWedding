package com.mywedding.weddingHall.repositories;


import com.mywedding.weddingHall.entities.WeddingHall;
import com.mywedding.weddingHall.entities.WeddingHallImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeddingHallImageRepository extends JpaRepository<WeddingHallImage, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM WeddingHallImage w WHERE w.id = :imageId")
    void deleteWeddingHallImageById(Long imageId);

    List<WeddingHallImage> findByWeddingHall(WeddingHall weddingHall);
    Optional<WeddingHallImage> findByName(String fileName);
}
