package com.example.zoutohanafansite.entity.nominatedreview;


import lombok.Data;

@Data
public class NominatedReviewWork {
    private long id;
    private String reviewTitle;
    private String userNickName;
    private int reviewVoteCount;

    public NominatedReviewWork(NominatedReview n){
        this.id = n.getId();
        this.reviewTitle = n.getReviewTitle();
        this.userNickName = n.getUserNickName();
        this.reviewVoteCount = n.getReviewVoteCount();
    }
}
