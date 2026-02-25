package com.example.zoutohanafansite.entity.form;

import lombok.Data;

@Data
public class IsbnForm {
    private long projectId;
    private String title;
    private String author;
    private String publisher;
    private long isbn;
}
