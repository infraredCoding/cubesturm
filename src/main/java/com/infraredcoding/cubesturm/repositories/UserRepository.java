package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SturmUser, Long> {
    Optional<SturmUser> findByUsername(String username);
}
