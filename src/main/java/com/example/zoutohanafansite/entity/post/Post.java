package com.example.zoutohanafansite.entity.post;

import com.example.zoutohanafansite.entity.enums.PostCategory;
import com.example.zoutohanafansite.entity.enums.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private long id;
    private int adminId;
    private PostCategory category;    // enums/PostCategory
    private String title;
    private String content;
    private LocalDateTime postedAt;
    private PostStatus status;  // enums/PostStatus
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
