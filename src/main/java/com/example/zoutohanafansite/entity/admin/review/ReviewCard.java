package com.example.zoutohanafansite.entity.admin.review;

import com.example.zoutohanafansite.entity.enums.UserGender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewCard {
    private long id;
    private Long projectId;
    private String projectUrlKey;
    private String projectName;
    private Long userId;
    private String userLoginId;
    private String userNickname;
    private String userAddress;
    private int userAgeGroup;
    private UserGender userGender;  // enums/UserGender
    private String userSelfIntroduction;
    private Long bookIsbn;
    private String bookTitle;
    private String bookPublisher;
    private String bookAuthor;
    private String reviewTitle;
    private String reviewContent;
    private String reviewContentEdited;
    private int voteCount;
    private boolean draft;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;
}
