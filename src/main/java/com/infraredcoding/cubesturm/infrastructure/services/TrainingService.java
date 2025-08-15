package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.training.TrainingSlot;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public interface TrainingService {
    Map<DayOfWeek, List<TrainingSlot>> getSlots(String username);
    TrainingSlot addSlot(TrainingSlot slot, String username);
    boolean updateSlot(TrainingSlot slot);
    boolean deleteSlot(Long id);
}
