package com.example.zoutohanafansite.entity.review;

import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.service.ProjectService;
import lombok.Data;

@Data
public class ReviewMyPage {
    private long id;
    private Project project;
    private String bookTitle;
    private String bookAuthor;
    private String reviewContent;
    private int voteCount;

    public ReviewMyPage(Review review, Project project) {
        this.id = review.getId();
        this.project = project;
        this.bookTitle = review.getBookTitle();
        this.bookAuthor = review.getBookAuthor();
        this.reviewContent = review.getReviewContent();
        this.voteCount = review.getVoteCount();
    }
}
