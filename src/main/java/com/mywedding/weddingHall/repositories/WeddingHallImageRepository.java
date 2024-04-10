package com.mywedding.weddingHall.repositories;


import com.mywedding.weddingHall.entities.WeddingHall;
import com.mywedding.weddingHall.entities.WeddingHallImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeddingHallImageRepository extends JpaRepository<WeddingHallImage, Long> {
    List<WeddingHallImage> findByWeddingHall(WeddingHall weddingHall);
}
