package com.infraredcoding.cubesturm.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class VideoUtils {
    public String processVideoToFastStart(String inputPath) throws InterruptedException, IOException {

        Path originalPath = Paths.get(inputPath);
        Path tempPath = Paths.get(inputPath.replace(".mp4", "_temp.mp4"));

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath,
                "-c", "copy",
                "-movflags", "faststart",
                "-y",
                tempPath.toString()
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(originalPath);
            throw new RuntimeException("FFmpeg processing failed with exit code " + exitCode);
        }

        Files.move(
                tempPath,
                originalPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return inputPath;
    }

    public void generateThumbnail(String inputPath, String outputPath) throws IOException, InterruptedException {
        Path uploadPath = Paths.get("uploads/thumbnails/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
            "ffmpeg",
            "-i", inputPath,
            "-ss", "00:00:02.000",
            "-vframes", "1",
            outputPath
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Failed to generate thumbnail");
        }
    }
}
