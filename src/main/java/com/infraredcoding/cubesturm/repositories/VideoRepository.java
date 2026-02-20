package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
