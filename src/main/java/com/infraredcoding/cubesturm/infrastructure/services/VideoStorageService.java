package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoStorageService {

    String savefile(MultipartFile file) throws IOException;
    Video saveVideo(Video video);
}
