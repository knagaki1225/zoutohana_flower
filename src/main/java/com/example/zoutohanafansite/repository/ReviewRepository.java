package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.admin.review.ReviewList;
import com.example.zoutohanafansite.entity.form.ReviewForm;
import com.example.zoutohanafansite.entity.form.ReviewSearchForm;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.entity.admin.review.ReviewCard;
import com.example.zoutohanafansite.mapper.ReviewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepository {
    private final ReviewMapper reviewMapper;

    public ReviewRepository(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public void insertReview(Review review) {
        reviewMapper.insertReview(review);
    }

    public Review selectReviewById(long id) {
        return reviewMapper.selectReviewById(id);
    }

    public void updateDraftReview(Review review){
        reviewMapper.updateDraftReview(review);
    }

    public void deleteReviewById(long id){
        reviewMapper.deleteReviewById(id);
    }

    public List<Review> selectReviewByUserId(long userId){
        return reviewMapper.selectReviewsByUserId(userId);
    }

    public ReviewCard selectReviewCardById(long userId) {
        return reviewMapper.selectReviewCardById(userId);
    }

    public List<ReviewCard> selectReviewCardsByUserId(long userId) {
        return reviewMapper.selectReviewCardsByUserId(userId);
    }

    public Long selectDraftId(long projectId, long userId){
        return reviewMapper.selectDraftId(projectId, userId);
    }

    public List<Review> selectAllReviewsNotDraftByUserId(long userId){
        return reviewMapper.selectAllReviewsNotDraftByUserId(userId);
    }

    public void updateReview(ReviewForm reviewForm, long id){
        reviewMapper.updateReview(reviewForm, id);
    }

    public List<Review> selectReviewsByUrlKey(String urlKey){
        return reviewMapper.selectReviewsByUrlKey(urlKey);
    }

    public void incrementVoteCount(long id){
        reviewMapper.incrementVoteCount(id);
    }

    public void decrementVoteCount(long id){
        reviewMapper.decrementVoteCount(id);
    }

    public List<Review> selectReviewByUrlKeyAndIdList(String urlKey, List<Long> idList){
        return reviewMapper.selectReviewByUrlKeyAndIdList(urlKey, idList);
    }

    public List<ReviewList> getReviewsByUrlKey(ReviewSearchForm form, String urlKey) {
        return reviewMapper.getReviewsByUrlKey(form, urlKey);
    }

    public void updateFirstStagePassed(List<Long> ids, boolean passed) {
        reviewMapper.updateFirstStagePassed(ids, passed);
    }

    public List<ReviewCard> selectReviewCardForExport(String urlKey) {
        return reviewMapper.selectReviewCardForExport(urlKey);
    }
}
