package com.example.zoutohanafansite.entity.form;

import lombok.Data;

@Data
public class ReviewForm {
    private long projectId;
    private String bookTitle;
    private String author;
    private String publisher;
    private long isbn;
    private String reviewTitle;
    private String reviewContent;
}
