package com.mywedding.weddingHall.repositories;

import com.mywedding.weddingHall.entities.UserReview;
import com.mywedding.weddingHall.entities.WeddingHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingCommentRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByWeddingHall(WeddingHall weddingHall);
}
