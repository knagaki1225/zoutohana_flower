package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReview;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReviewWork;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.entity.review.ReviewAward;
import com.example.zoutohanafansite.repository.NominatedReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NominatedReviewService {
    private final NominatedReviewRepository nominatedReviewRepository;
    private final ReviewService reviewService;

    public NominatedReviewService(NominatedReviewRepository nominatedReviewRepository, ReviewService reviewService) {
        this.nominatedReviewRepository = nominatedReviewRepository;
        this.reviewService = reviewService;
    }

    public List<NominatedReview> getByProjectId(long projectId){
        return nominatedReviewRepository.selectByProjectId(projectId);
    }


    public List<NominatedReviewWork> getNominatedReviewsByProjectId(long projectId){
        List<NominatedReview> nominatedReviews = getByProjectId(projectId);
        List<NominatedReviewWork> nominatedReviewWorks = new ArrayList<>();
        for (NominatedReview nominatedReview : nominatedReviews) {
            NominatedReviewWork nominatedReviewWork = new NominatedReviewWork(nominatedReview);
            nominatedReviewWorks.add(nominatedReviewWork);
        }
        return nominatedReviewWorks;
    }

    public NominatedReview getAwardReviewByUrlKey(String urlKey){
        return nominatedReviewRepository.selectAwardReviewByUrlKey(urlKey);
    }

    public List<NominatedReview> getNotAwardReviewsByUrlKey(String urlKey){
        return nominatedReviewRepository.selectNotAwardReviewByUrlKey(urlKey);
    }

    public List<ReviewAward> getNotAwardNominatedReviewsByUrlKey(String urlKey){
        List<NominatedReview> nominatedReviews = getNotAwardReviewsByUrlKey(urlKey);
        List<ReviewAward> reviewAwards = new ArrayList<>();
        int i = 0;
        for (NominatedReview nominatedReview : nominatedReviews) {
            ReviewAward reviewAward = new ReviewAward(nominatedReview, "/api/image/book" + (i % 4 + 1) + ".png", "/api/image/icon" + nominatedReview.getUserIcon() + ".png");
            reviewAwards.add(reviewAward);
            i++;
        }
        return reviewAwards;
    }

    public List<ReviewAward> getParticipation(String urlKey){
        List<NominatedReview> nominatedReviews = getNotAwardReviewsByUrlKey(urlKey);
        List<Review> allParticipation = reviewService.selectReviewsByUrlKey(urlKey);
        Set<Long> nominatedIds = nominatedReviews.stream()
                .map(NominatedReview::getReviewId)
                .collect(Collectors.toSet());

        allParticipation.removeIf(review -> nominatedIds.contains(review.getId()));

        List<ReviewAward> reviewAwards = new ArrayList<>();
        for (int i = 0; i < allParticipation.size(); i++) {
            Review review = allParticipation.get(i);
            reviewAwards.add(new ReviewAward(
                    review,
                    "/api/image/book" + (i % 4 + 1) + ".png",
                    "/api/image/icon" + review.getUserIcon() + ".png"
            ));
        }

        return reviewAwards;
    }

    /**
     * 指定したidのprojectの、nominated_reviewsを全取得(企画編集・情報用)
     *
     * @param projectId
     * @return List<NominatedReviewCard>
     */
    public List<NominatedReviewCard> getNominatedReviewCardByProjectId(long projectId) {
        return nominatedReviewRepository.selectNominatedReviewCardByProjectId(projectId);
    }

    /**
     * 指定したidのprojectの、nominated_reviewsの中でawardedがtrueの書評を全取得(企画編集・情報用)
     *
     * @param projectId
     * @return List<NominatedReviewCard>
     */
    public List<NominatedReviewCard> getAwardedReviewCardByProjectId(long projectId) {
        return nominatedReviewRepository.selectAwardedReviewCardByProjectId(projectId);
    }
}
