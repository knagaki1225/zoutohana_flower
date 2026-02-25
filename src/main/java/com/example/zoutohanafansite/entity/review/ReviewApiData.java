package com.example.zoutohanafansite.entity.review;

import com.example.zoutohanafansite.service.ProjectService;
import lombok.Data;

@Data
public class ReviewApiData {
    private long id;
    private String title;
    private String image;
    private String content;
    private String name;
    private String age;
    private String gender;
    private String address;
    private String text;
    private Integer voteCount;
    private String icon;

    public ReviewApiData(Review review, String imageUrl) {
        this.id = review.getId();
        this.title = review.getReviewTitle();
        this.image = imageUrl;
        this.content = review.getReviewContent();
        this.name = review.getUserNickname();
        this.age = review.getUserAgeGroup() + "代";
        this.gender = review.getUserGender().getLabel();
        this.address = review.getUserAddress();
        this.text = review.getUserSelfIntroduction();
        this.voteCount = review.getVoteCount();
        this.icon = "/api/image/icon" + review.getUserIcon() + ".png";
    }
}
