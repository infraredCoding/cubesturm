package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VideoStorageService {

    String savefile(MultipartFile file) throws IOException;
    Video saveVideo(Video video);
    List<Video> getAllVideos(String username);
    Video getVideoById(Long id);
}
