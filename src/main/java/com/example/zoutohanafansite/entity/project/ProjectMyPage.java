package com.example.zoutohanafansite.entity.project;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.ThemeColor;
import lombok.Data;

@Data
public class ProjectMyPage {
    private long id;
    private String name;
    private String urlKey;
    private ThemeColor themeColor;
    private ProjectStatus status;
    private String mainImgUrl;
    private Integer lastDate;
    private boolean draft;

    public ProjectMyPage(Project project, Integer lastDate, boolean draft) {
        this.id = project.getId();
        this.name = project.getName();
        this.urlKey = project.getUrlKey();
        this.themeColor = project.getThemeColor();
        this.status = project.getStatus();
        this.mainImgUrl = project.getMainImgUrl();
        this.lastDate = lastDate;
        this.draft = draft;
    }
}
