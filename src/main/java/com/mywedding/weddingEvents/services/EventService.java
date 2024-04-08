package com.mywedding.weddingEvents.services;

import com.mywedding.weddingEvents.dto.dtoRequests.AddEventRequest;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity<Object> createEvent(AddEventRequest addEventRequest);
}
