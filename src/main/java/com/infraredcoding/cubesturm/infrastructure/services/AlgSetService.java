package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.dto.training.AlgSetListDto;
import com.infraredcoding.cubesturm.domain.models.training.Alg;
import com.infraredcoding.cubesturm.domain.models.training.AlgSet;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface AlgSetService {
    List<AlgSetListDto> getAlgSetList(String username);
    Optional<AlgSet> getAlgSetById(Long id);
    AlgSet createAlgSet(AlgSet algSet);

    List<Alg> createAlgsInBulk(List<Alg> algs);
    boolean deleteAlgSet(Long id);
}
