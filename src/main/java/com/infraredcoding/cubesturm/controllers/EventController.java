package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.models.puzzles.Event;
import com.infraredcoding.cubesturm.infrastructure.services.EventService;
import com.infraredcoding.cubesturm.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/events")
@Service
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(eventService.getAllEvents(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Event>> getEventDetails(@PathVariable("id") Long id) {
        Optional<Event> e = eventService.getEventById(id);
        if (e.isPresent()) {
            return new ResponseEntity<>(e, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@AuthenticationPrincipal UserDetails user, @RequestBody Event event) {
        return new ResponseEntity<>(eventService.createEvent(event, user.getUsername()), HttpStatus.CREATED);
    }

}
