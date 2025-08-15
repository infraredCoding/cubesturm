package com.infraredcoding.cubesturm.domain.models.training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infraredcoding.cubesturm.domain.enums.Priority;
import com.infraredcoding.cubesturm.domain.enums.Puzzle;
import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSlot {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private Puzzle puzzle;
    private Priority priority;
    private String duration;
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
    @JsonIgnore
    private SturmUser user;
}
