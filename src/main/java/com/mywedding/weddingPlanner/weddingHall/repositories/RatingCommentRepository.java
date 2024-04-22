package com.mywedding.weddingPlanner.weddingHall.repositories;

import com.mywedding.weddingPlanner.plannerBaseEntities.ServiceEntity;
import com.mywedding.weddingPlanner.plannerBaseEntities.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingCommentRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByWeddingHall(ServiceEntity serviceEntity);
}
