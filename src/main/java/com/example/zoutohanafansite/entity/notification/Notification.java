package com.example.zoutohanafansite.entity.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private long id;
    private long adminId;
    private long userId;
    private long reviewId;
    private String title;
    private String content;
    private boolean seen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
