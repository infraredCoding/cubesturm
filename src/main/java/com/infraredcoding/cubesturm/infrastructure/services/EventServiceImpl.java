package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.puzzles.Event;
import com.infraredcoding.cubesturm.repositories.EventRepository;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<Event> getAllEvents(String username) {
        return eventRepository.findByCreatedBy(username);
    }

    @Override
    public Optional<Event> getEventById(long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event createEvent(Event event, String username) {
        event.setUser(userRepository.findByUsername(username).orElse(null));
        return eventRepository.save(event);
    }

    @Override
    public boolean updateEvent(Event event) {
        Optional<Event> _event = eventRepository.findById(event.getId());
        if (_event.isPresent()) {
            eventRepository.save(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEvent(long id) {
        Optional<Event> _event = eventRepository.findById(id);
        if(_event.isPresent()) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
