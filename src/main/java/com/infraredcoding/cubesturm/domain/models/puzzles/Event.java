package com.infraredcoding.cubesturm.domain.models.puzzles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infraredcoding.cubesturm.domain.enums.Puzzle;
import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private long id;
    private Puzzle puzzle;
    private String main;
    private String goal;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
    @JsonIgnore
    private SturmUser user;
}
