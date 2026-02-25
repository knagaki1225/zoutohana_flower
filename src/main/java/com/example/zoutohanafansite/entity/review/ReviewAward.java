package com.example.zoutohanafansite.entity.review;

import com.example.zoutohanafansite.entity.nominatedreview.NominatedReview;
import lombok.Data;

@Data
public class ReviewAward {
    private long id;
    private String title; //reviewTitle
    private String image;
    private String content; // reviewContent
    private String icon;
    private String name; //userName
    private int vote;
    private String age;
    private String gender;
    private String address;
    private String text; //userSelfIntroduction

    public ReviewAward(NominatedReview n, String imageUrl, String iconUrl) {
        this.id = n.getReviewId();
        this.title = n.getReviewTitle();
        this.image = imageUrl;
        this.content = n.getReviewContent();
        this.icon = iconUrl;
        this.name = n.getUserNickName();
        this.vote = n.getReviewVoteCount();
        this.age = n.getUserAgeGroup() + "代";
        this.gender = n.getUserGender().getLabel();
        this.address = n.getUserAddress();
        this.text = n.getUserSelfIntroduction();
    }

    public ReviewAward(Review review, String imageUrl, String iconUrl) {
        this.id = review.getId();
        this.title = review.getReviewTitle();
        this.image = imageUrl;
        this.content = review.getReviewContent();
        this.icon = iconUrl;
        this.name = review.getUserNickname();
        this.vote = review.getVoteCount();
        this.age = review.getUserAgeGroup() + "代";
        this.gender = review.getUserGender().getLabel();
        this.address = review.getUserAddress();
        this.text = review.getUserSelfIntroduction();
    }
}
