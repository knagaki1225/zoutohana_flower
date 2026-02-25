package com.example.zoutohanafansite.entity.post;

import com.example.zoutohanafansite.entity.enums.PostCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostTop {
    private long id;
    private PostCategory category;
    private String title;
    private LocalDateTime postedAt;

    public PostTop(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.postedAt = post.getPostedAt();
    }
}
