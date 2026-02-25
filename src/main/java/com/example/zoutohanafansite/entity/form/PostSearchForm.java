package com.example.zoutohanafansite.entity.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostSearchForm {
    private String sort;
    private String keyword;

    private List<String> status;
    private List<String> category;
    private LocalDateTime createdStartAt;
    private LocalDateTime createdEndAt;
    private LocalDateTime postedStartAt;
    private LocalDateTime postedEndAt;
}
