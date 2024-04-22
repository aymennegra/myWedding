package com.mywedding.weddingPlanner.weddingHall.repositories;


import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceEntity;
import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceImages;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeddingHallImageRepository extends JpaRepository<ServiceImages, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ServiceImages w WHERE w.id = :imageId")
    void deleteWeddingHallImageById(Long imageId);

    List<ServiceImages> findByWeddingHall(ServiceEntity serviceEntity);
    Optional<ServiceImages> findByName(String fileName);
}
