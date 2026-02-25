package com.example.zoutohanafansite.entity.nominatedreview;

import com.example.zoutohanafansite.entity.enums.UserGender;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NominatedReview {
    private long id;
    private long projectId;
    private long userId;
    private String userNickName;
    private String userAddress;
    private int userAgeGroup;
    private UserGender userGender;
    private String userSelfIntroduction;
    private int userIcon;
    private long reviewId;
    private String reviewTitle;
    private String reviewContent;
    private String reviewContentEdited;
    private int reviewVoteCount;
    private boolean reviewAwarded;
    private long bookIsbn;
    private String bookTitle;
    private String bookPublisher;
    private String bookAuthor;
    private String adminComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
