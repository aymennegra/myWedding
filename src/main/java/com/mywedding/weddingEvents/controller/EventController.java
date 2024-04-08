package com.mywedding.weddingEvents.controller;


import com.mywedding.weddingEvents.dto.dtoRequests.AddEventRequest;
import com.mywedding.weddingEvents.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("admin/create")
    public ResponseEntity<Object> createEvent (@RequestBody AddEventRequest addEventRequest) {
        // Call the updateUserProfile() method from AuthenticationService and return the result
        return eventService.createEvent(addEventRequest);
    }
}
