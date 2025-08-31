package com.infraredcoding.cubesturm.domain.models.training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alg {
    @Id
    @GeneratedValue
    private long id;
    private String algorithm;

    @ManyToOne
    @JoinColumn(name="algset_id", referencedColumnName = "id", nullable=false)
    @JsonIgnore
    private AlgSet algset;

    public String getScramble(){
        return algorithm;
    }
}
