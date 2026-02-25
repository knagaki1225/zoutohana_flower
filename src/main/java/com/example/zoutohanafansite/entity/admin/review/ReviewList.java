package com.example.zoutohanafansite.entity.admin.review;

import com.example.zoutohanafansite.entity.enums.UserGender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewList {
    private long id;
    private Long projectId;
    private Long userId;
    private String userNickname;
    private String userAddress;
    private int userAgeGroup;
    private UserGender userGender;
    private String bookTitle;
    private String reviewTitle;
    private int voteCount;
    private String status;
    private LocalDateTime createdAt;
}
