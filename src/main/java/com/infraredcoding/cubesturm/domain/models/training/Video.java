package com.infraredcoding.cubesturm.domain.models.training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infraredcoding.cubesturm.domain.models.core.SturmUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String fileName;
    private String filePath;
    private Integer duration;
    private Long size;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private SturmUser uploadedBy;

    public String getThumbnail(){
        return this.getFilePath().replace(".mp4", ".jpg").replace("videos", "thumbnails");
    }
}
