package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.dto.training.AlgSetListDto;
import com.infraredcoding.cubesturm.domain.enums.Puzzle;
import com.infraredcoding.cubesturm.domain.models.training.Alg;
import com.infraredcoding.cubesturm.domain.models.training.AlgSet;
import com.infraredcoding.cubesturm.domain.models.training.TrainingSlot;
import com.infraredcoding.cubesturm.infrastructure.services.AlgSetService;
import com.infraredcoding.cubesturm.infrastructure.services.TrainingService;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.util.*;

@RestController
@RequestMapping("api/v1/training")
@Service
@RequiredArgsConstructor
public class TrainingController {
    public final TrainingService trainingService;
    private final AlgSetService algSetService;
    private final UserRepository userRepository;

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

    @GetMapping("algset")
    public ResponseEntity<List<AlgSetListDto>> getAlgSetList(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(algSetService.getAlgSetList(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("algset/{id}")
    public ResponseEntity<AlgSet> getAlgSet(@PathVariable("id") Long id) {
        Optional<AlgSet> as = algSetService.getAlgSetById(id);
        if (as.isPresent()){
            return new ResponseEntity<>(as.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "algset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadCsv(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("file") MultipartFile file, @RequestParam("title") String title,
            @RequestParam("puzzle") Puzzle puzzle
    ) {
        List<String> algorithms = new ArrayList<>();
        List<Integer> algIndexes = new ArrayList<>();
        boolean headerFound = false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int rowIndex = 0;

            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",", -1); // -1 to preserve empty cells

                if (!headerFound) {
                    // Look for "ALG" in this row
                    for (int i = 0; i < cells.length; i++) {
                        if ("alg".equalsIgnoreCase(cells[i].trim())) {
                            algIndexes.add(i);
                        }
                    }
                    if (!algIndexes.isEmpty()) {
                        headerFound = true; // next rows will be data
                    }
                } else {
                    // Extract from algIndexes
                    for (int idx : algIndexes) {

                        if (idx < cells.length) {
                            String value = cells[idx].trim();
                            if (!value.isEmpty()) {
                                // If multiple lines inside the cell, take only the first
                                String firstAlg = value.split("\\R", 2)[0].trim();
                                if (!firstAlg.isEmpty()) {
                                    algorithms.add(firstAlg);
                                }
                            }
                        }
                    }
                }

                rowIndex++;
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonList("Error parsing file: " + e.getMessage()));
        }

        if (algIndexes.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonList("No ALG column found in the file"));
        }

        AlgSet as = new AlgSet();
        as.setTitle(title); as.setPuzzle(puzzle);
        as.setUser(userRepository.findByUsername(user.getUsername()).orElse(null));
        AlgSet created = algSetService.createAlgSet(as);
        List<Alg> algList = new ArrayList<>();

        for (String alg : algorithms) {
            Alg a = new Alg();
            a.setAlgorithm(alg); a.setAlgset(created);
            algList.add(a);
        }

        algSetService.createAlgsInBulk(algList);


        return ResponseEntity.ok(algorithms);
    }
}
