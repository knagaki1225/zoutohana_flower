package com.example.zoutohanafansite.entity.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewSearchForm {
    private String sort;
    private String keyword;

    private List<String> status;
    private List<String> userGender;
    private List<String> userAgeGroup;
    private List<String> userAddress;
    private List<Long> genreIds;
    private LocalDateTime createdStartAt;
    private LocalDateTime createdEndAt;
}
