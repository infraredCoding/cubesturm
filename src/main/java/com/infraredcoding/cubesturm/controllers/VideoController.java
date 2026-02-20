package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import com.infraredcoding.cubesturm.infrastructure.services.VideoStorageService;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/videos")
@Service
@RequiredArgsConstructor
public class VideoController {
    private final VideoStorageService videoStorageService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @AuthenticationPrincipal UserDetails user
    ) {
        try {
            String filePath = videoStorageService.savefile(file);

            Video video = new Video();
            video.setTitle(title);
            video.setFileName(file.getOriginalFilename());
            video.setFilePath(filePath);
            video.setSize(file.getSize());
            video.setUploadedBy(userRepository.findByUsername(user.getUsername()).orElse(null));
            video.setCreationDate(LocalDateTime.now());

            return new ResponseEntity<>(videoStorageService.saveVideo(video), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
