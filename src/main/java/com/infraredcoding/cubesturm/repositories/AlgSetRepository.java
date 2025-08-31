package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.dto.training.AlgSetListDto;
import com.infraredcoding.cubesturm.domain.models.training.AlgSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlgSetRepository extends JpaRepository<AlgSet, Long> {
    @Query(
        "select new com.infraredcoding.cubesturm.domain.dto.training.AlgSetListDto(e.id, e.title, e.puzzle) " +
        "from AlgSet e " +
        "Join e.user u where u.username = :username"
    )
    List<AlgSetListDto> findByCreatedBy(@Param("username") String username);

    @Query("SELECT a FROM AlgSet a LEFT JOIN FETCH a.algorithms WHERE a.id = :id")
    Optional<AlgSet> findByIdWithAlgorithms(@Param("id") Long id);
}
