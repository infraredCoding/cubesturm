package com.infraredcoding.cubesturm.repositories;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query(
            "select v from Video v " +
            "join v.uploadedBy u where u.username = :username"
    )
    List<Video> findByUsername(@Param("username") String username);
}
