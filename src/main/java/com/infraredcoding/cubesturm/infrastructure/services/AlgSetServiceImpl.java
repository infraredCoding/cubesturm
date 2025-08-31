package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.dto.training.AlgSetListDto;
import com.infraredcoding.cubesturm.domain.models.training.Alg;
import com.infraredcoding.cubesturm.domain.models.training.AlgSet;
import com.infraredcoding.cubesturm.repositories.AlgRepository;
import com.infraredcoding.cubesturm.repositories.AlgSetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlgSetServiceImpl implements AlgSetService{
    private final AlgSetRepository algSetRepository;
    private final AlgRepository algRepository;

    @Override
    public List<AlgSetListDto> getAlgSetList(String username) {
        return algSetRepository.findByCreatedBy(username);
    }

    @Override
    public Optional<AlgSet> getAlgSetById(Long id) {
        return algSetRepository.findByIdWithAlgorithms(id);
    }

    @Override
    public AlgSet createAlgSet(AlgSet algSet) {
        return algSetRepository.save(algSet);
    }

    @Override
    public List<Alg> createAlgsInBulk(List<Alg> algs) {
        return algRepository.saveAll(algs);
    }

    @Override
    public boolean deleteAlgSet(Long id) {
        Optional<AlgSet> algSet = algSetRepository.findById(id);
        if (algSet.isPresent()) {
            algSetRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
