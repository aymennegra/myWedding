package com.mywedding.weddingEvents.services.impl;

import com.mywedding.identity.dto.dtoResponses.ResponseHandler;
import com.mywedding.identity.entities.User;
import com.mywedding.identity.repository.UserRepository;
import com.mywedding.weddingEvents.dto.dtoRequests.AddEventRequest;
import com.mywedding.weddingEvents.dto.dtoResponses.AddEventResponse;
import com.mywedding.weddingEvents.entities.WeddingEvent;
import com.mywedding.weddingEvents.repositories.EventRepository;
import com.mywedding.weddingEvents.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> createEvent(AddEventRequest addEventRequest) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Check if the user exists in the database based on the email (assuming email is the username)
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user.isAdmin())
            {
                //save wedding event info
                WeddingEvent weddingEvent = new WeddingEvent();
                weddingEvent.setName(addEventRequest.getName());
                weddingEvent.setLocation(addEventRequest.getLocation());
                weddingEvent.setPrice(addEventRequest.getPrice());
                weddingEvent.setDescription(addEventRequest.getDescription());
                weddingEvent.setCreatedBy(user);
                eventRepository.save(weddingEvent);
                AddEventResponse addEventResponse = new AddEventResponse();
                addEventResponse.setId(weddingEvent.getId());
                addEventResponse.setName(addEventRequest.getName());
                addEventResponse.setLocation(addEventRequest.getLocation());
                addEventResponse.setPrice(addEventRequest.getPrice());
                addEventResponse.setDescription(addEventRequest.getDescription());
                addEventResponse.setCreatedBy(user);

                return ResponseHandler.responseBuilder("Event created", HttpStatus.OK,
                        addEventResponse);
            }else {
                return ResponseHandler.responseBuilder("sorry, you are not allowed to create events..", HttpStatus.UNAUTHORIZED, new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseHandler.responseBuilder("Unauthorized", HttpStatus.UNAUTHORIZED, new ArrayList<>());
        }
    }
}
