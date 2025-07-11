package com.infraredcoding.cubesturm.domain.models.core;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table
@Data
@RequiredArgsConstructor
public class SturmUser {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String wcaId;
    private String username;
    private String email;
    private String password;
}
