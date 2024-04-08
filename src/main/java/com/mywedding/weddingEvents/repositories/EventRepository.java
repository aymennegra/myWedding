package com.mywedding.weddingEvents.repositories;

import com.mywedding.weddingEvents.entities.WeddingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<WeddingEvent,Long> {
}
