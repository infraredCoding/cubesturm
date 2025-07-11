package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.models.puzzles.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(
        "select e from Event e " +
        "Join e.user u where u.username = :username"
    )
    List<Event> findByCreatedBy(@Param("username") String username);
}
