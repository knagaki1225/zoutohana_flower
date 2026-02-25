package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.security.CustomAdminUserDetails;
import com.example.zoutohanafansite.security.CustomUserDetails;
import com.example.zoutohanafansite.service.NotificationService;
import com.example.zoutohanafansite.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final NotificationService notificationService;
    private final UserService userService;

    public GlobalControllerAdvice(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @ModelAttribute("unreadCount")
    public int getUnreadMailCount(@AuthenticationPrincipal Object principal){
        if(principal == null || principal instanceof CustomAdminUserDetails admin){
            return 0;
        }

        if (principal instanceof CustomUserDetails userDetails) {
            return notificationService.getSeenNum(userDetails.getUserId());
        }

        return 0;
    }
}
