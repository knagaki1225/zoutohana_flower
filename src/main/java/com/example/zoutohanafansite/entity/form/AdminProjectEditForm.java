package com.example.zoutohanafansite.entity.form;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.ThemeColor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminProjectEditForm {
    private Long id;
    private boolean published;
    private ProjectStatus status;
    private String name;
    private String urlKey;
    private String introduction;
    private ThemeColor themeColor;
    private LocalDateTime projectStartAt;
    private LocalDateTime projectEndAt;
    private LocalDateTime submissionStartAt;
    private LocalDateTime submissionEndAt;
    private LocalDateTime votingStartAt;
    private LocalDateTime votingEndAt;
}
