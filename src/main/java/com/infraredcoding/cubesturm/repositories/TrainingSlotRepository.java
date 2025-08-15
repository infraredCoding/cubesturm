package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.models.training.TrainingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingSlotRepository extends JpaRepository<TrainingSlot, Long> {
    @Query("""
        SELECT ts.dayOfWeek, ts
        FROM TrainingSlot ts
        WHERE ts.user.username = :username
        ORDER BY ts.dayOfWeek, ts.priority
    """)
    List<Object[]> findByCreatedBy(@Param("username") String username);
}
