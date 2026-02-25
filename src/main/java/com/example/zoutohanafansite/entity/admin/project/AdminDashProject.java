package com.example.zoutohanafansite.entity.admin.project;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.ThemeColor;
import com.example.zoutohanafansite.entity.project.Project;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDashProject {
    private long id;
    private String name;
    private String urlKey;
    private String mainImgUrl;
    private ThemeColor themeColor;  // enums/ThemeColor
    private ProjectStatus status;  // enums/ProjectStatus
    private LocalDateTime projectStartAt;
    private LocalDateTime projectEndAt;
    private LocalDateTime submissionStartAt;
    private LocalDateTime submissionEndAt;
    private LocalDateTime votingStartAt;
    private LocalDateTime votingEndAt;

    public AdminDashProject(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.urlKey = project.getUrlKey();
        this.mainImgUrl = project.getMainImgUrl();
        this.themeColor = project.getThemeColor();
        this.status = project.getStatus();
        this.projectStartAt = project.getProjectStartAt();
        this.projectEndAt = project.getProjectEndAt();
        this.submissionStartAt = project.getSubmissionStartAt();
        this.submissionEndAt = project.getSubmissionEndAt();
        this.votingStartAt = project.getVotingStartAt();
        this.votingEndAt = project.getVotingEndAt();
    }
}
