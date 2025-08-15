package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.training.TrainingSlot;
import com.infraredcoding.cubesturm.repositories.TrainingSlotRepository;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingSlotRepository trainingSlotRepository;
    private final UserRepository userRepository;

    @Override
    public Map<DayOfWeek, List<TrainingSlot>> getSlots(String username) {
        List<Object[]> raw = trainingSlotRepository.findByCreatedBy(username);

        return raw.stream()
            .collect(Collectors.groupingBy(
                    obj -> (DayOfWeek) obj[0],
                    LinkedHashMap::new,
                    Collectors.mapping(obj -> (TrainingSlot) obj[1], Collectors.toList())
            ));
    }

    @Override
    public TrainingSlot addSlot(TrainingSlot slot, String username) {
        slot.setUser(userRepository.findByUsername(username).orElse(null));
        return trainingSlotRepository.save(slot);
    }

    @Override
    public boolean updateSlot(TrainingSlot slot) {
        Optional<TrainingSlot> optional = trainingSlotRepository.findById(slot.getId());
        if (optional.isPresent()) {
            TrainingSlot trainingSlot = optional.get();
            trainingSlot.setTitle(slot.getTitle());
            trainingSlot.setPriority(slot.getPriority());
            trainingSlot.setDuration(slot.getDuration());
            trainingSlot.setDayOfWeek(slot.getDayOfWeek());

            trainingSlotRepository.save(trainingSlot);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteSlot(Long id) {
        Optional<TrainingSlot> optional = trainingSlotRepository.findById(id);
        if (optional.isPresent()) {
            trainingSlotRepository.delete(optional.get());
            return true;
        }
        return false;
    }
}
