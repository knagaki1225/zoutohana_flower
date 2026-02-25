package com.example.zoutohanafansite.entity.form;

import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectSearchForm {
    private String sort;
    private String keyword;

    private List<String> status;
    private List<String> published;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
