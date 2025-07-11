package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.puzzles.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EventService {
    List<Event> getAllEvents(String username);
    Optional<Event> getEventById(long id);
    Event createEvent(Event event, String username);
    boolean updateEvent(Event event);
    boolean deleteEvent(long id);
}
