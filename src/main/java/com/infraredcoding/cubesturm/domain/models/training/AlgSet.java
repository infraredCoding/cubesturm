package com.infraredcoding.cubesturm.domain.models.training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infraredcoding.cubesturm.domain.enums.Puzzle;
import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlgSet {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private Puzzle puzzle;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable=false)
    @JsonIgnore
    private SturmUser user;

    @OneToMany(mappedBy = "algset", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Alg> algorithms = new ArrayList<>();
}
