package com.infraredcoding.cubesturm.infrastructure.services;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import com.infraredcoding.cubesturm.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoStorageServiceImpl implements VideoStorageService {
    private final String uploadDir = "uploads/videos/";
    private final VideoRepository videoRepository;

    @Override
    public String savefile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @Override
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public List<Video> getAllVideos(String username) {
        return videoRepository.findByUsername(username);
    }

    @Override
    public Video getVideoById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

}
