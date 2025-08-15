package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.models.training.TrainingSlot;
import com.infraredcoding.cubesturm.infrastructure.services.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/training")
@Service
@RequiredArgsConstructor
public class TrainingController {
    public final TrainingService trainingService;

    @GetMapping("slots")
    public ResponseEntity<Map<DayOfWeek, List<TrainingSlot>>> getSlots(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(trainingService.getSlots(user.getUsername()), HttpStatus.OK);
    }

    @PostMapping("slots")
    public ResponseEntity<TrainingSlot> createSlot(@RequestBody TrainingSlot slot, @AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(trainingService.addSlot(slot, user.getUsername()), HttpStatus.CREATED);
    }

    @PatchMapping("slots/{id}")
    public ResponseEntity<TrainingSlot> updateSlot(@RequestBody TrainingSlot slot, @PathVariable("id") long id) {
        boolean res = trainingService.updateSlot(slot);
        if (res){
            return new ResponseEntity<>(slot, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("slots/{id}")
    public ResponseEntity<TrainingSlot> deleteSlot(@PathVariable("id") long id) {
        boolean res = trainingService.deleteSlot(id);
        if (res){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
