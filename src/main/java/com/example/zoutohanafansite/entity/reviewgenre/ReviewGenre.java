package com.example.zoutohanafansite.entity.reviewgenre;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewGenre {
    private Long reviewId;
    private Long genreId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;
}
