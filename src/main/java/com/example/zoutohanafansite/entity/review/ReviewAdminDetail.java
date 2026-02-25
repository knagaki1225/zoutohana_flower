package com.example.zoutohanafansite.entity.review;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewAdminDetail {
    private long id;
    private String reviewTitle;
    private String projectName;
    private LocalDate createDate;
    private LocalDate updateDate;

}
