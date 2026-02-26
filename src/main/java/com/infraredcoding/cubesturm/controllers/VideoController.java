package com.infraredcoding.cubesturm.controllers;

import com.infraredcoding.cubesturm.domain.models.training.Video;
import com.infraredcoding.cubesturm.infrastructure.services.VideoStorageService;
import com.infraredcoding.cubesturm.infrastructure.utils.VideoUtils;
import com.infraredcoding.cubesturm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/videos")
@Service
@RequiredArgsConstructor
public class VideoController {
    private static final long CHUNK_SIZE =  1024 * 1024; // 1MB

    private final VideoStorageService videoStorageService;
    private final UserRepository userRepository;

    private final VideoUtils videoUtils;

    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("duration") int duration,
            @AuthenticationPrincipal UserDetails user
    ) {
        try {
            String filePath = videoStorageService.savefile(file);

            videoUtils.processVideoToFastStart(filePath);

            Video video = new Video();
            video.setTitle(title);
            video.setFileName(file.getOriginalFilename());
            video.setFilePath(filePath);
            video.setSize(file.getSize());
            video.setUploadedBy(userRepository.findByUsername(user.getUsername()).orElse(null));
            video.setDuration(duration);
            video.setCreationDate(LocalDateTime.now());


            return new ResponseEntity<>(videoStorageService.saveVideo(video), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(videoStorageService.getAllVideos(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(videoStorageService.getVideoById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> getVideoStreamById(
            @PathVariable("id") Long id, @RequestHeader HttpHeaders headers
    ) throws IOException {
        Video video = videoStorageService.getVideoById(id);
        if (video == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource videoResource = new FileSystemResource(video.getFilePath());
        long contentLength = videoResource.contentLength();

        if (headers.getRange().isEmpty()){
            ResourceRegion region = new ResourceRegion(videoResource, 0, Math.min(CHUNK_SIZE, contentLength));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.valueOf("video/mp4"))
                    .header("Accept-Ranges", "bytes")
                    .body(region);
        }

        HttpRange range = headers.getRange().get(0);
        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);
        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);

        ResourceRegion region =
                new ResourceRegion(videoResource, start, rangeLength);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.valueOf("video/mp4"))
                .header("Accept-Ranges", "bytes")
                .body(region);
    }
}
