package com.example.zoutohanafansite.entity.genre;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Genre {
    private long id;
    private String name;
    private String furigana;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
