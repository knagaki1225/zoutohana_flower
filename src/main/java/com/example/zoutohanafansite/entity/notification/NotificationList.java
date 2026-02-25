package com.example.zoutohanafansite.entity.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationList {
    private long id;
    private String title;
    private LocalDateTime sendAt;

    public NotificationList(Notification n) {
        this.id = n.getId();
        this.title = n.getTitle();
        this.sendAt = n.getCreatedAt();
    }
}
