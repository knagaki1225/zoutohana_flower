package com.example.zoutohanafansite.entity.project;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.ThemeColor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectTop {
    private long id;
    private String name;
    private String urlKey;
    private String introduction;
    private String mainImgUrl;
    private ThemeColor themeColor;
    private LocalDateTime projectStartAt;
    private LocalDateTime projectEndAt;
    private ProjectStatus projectStatus;

    public ProjectTop(long id, String name, String urlKey, String introduction, String mainImgUrl, ThemeColor themeColor, LocalDateTime projectStartAt, LocalDateTime projectEndAt, ProjectStatus projectStatus) {
        this.id = id;
        this.name = name;
        this.urlKey = urlKey;
        this.introduction = introduction;
        this.mainImgUrl = mainImgUrl;
        this.themeColor = themeColor;
        this.projectStartAt = projectStartAt;
        this.projectEndAt = projectEndAt;
        this.projectStatus = projectStatus;
    }
}
