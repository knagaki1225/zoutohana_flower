package com.example.zoutohanafansite.entity.admin.notification;

import lombok.Data;

@Data
public class NotificationSend {
    private int recipient;
    private String urlKey;
    private String title;
    private String message;
}
