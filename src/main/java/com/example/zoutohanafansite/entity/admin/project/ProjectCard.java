package com.example.zoutohanafansite.entity.admin.project;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.ThemeColor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectCard {
    private long id;
    private String name;
    private String urlKey;
    private String introduction;
    private String mainImgUrl;
    private ThemeColor themeColor;  // enums/ThemeColor
    private ProjectStatus status;  // enums/ProjectStatus
    private boolean enableVisibleBookTitle;
    private boolean enableVisibleReviewTitle;
    private boolean enableVisibleUserInfo;
    private boolean published;
    private LocalDateTime projectStartAt;
    private LocalDateTime projectEndAt;
    private LocalDateTime submissionStartAt;
    private LocalDateTime submissionEndAt;
    private LocalDateTime votingStartAt;
    private LocalDateTime votingEndAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long reviewCount;
}
