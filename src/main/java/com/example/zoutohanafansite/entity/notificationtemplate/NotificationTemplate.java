package com.example.zoutohanafansite.entity.notificationtemplate;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationTemplate {
    private long id;
    private String name;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
