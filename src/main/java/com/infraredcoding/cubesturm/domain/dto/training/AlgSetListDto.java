package com.infraredcoding.cubesturm.domain.dto.training;

import com.infraredcoding.cubesturm.domain.enums.Puzzle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlgSetListDto {
    private long id;
    private String title;
    private Puzzle puzzle;
}
