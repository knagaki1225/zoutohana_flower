package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReview;
import com.example.zoutohanafansite.mapper.NominatedReviewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NominatedReviewRepository {
    private final NominatedReviewMapper nominatedReviewMapper;

    public NominatedReviewRepository(NominatedReviewMapper nominatedReviewMapper) {
        this.nominatedReviewMapper = nominatedReviewMapper;
    }

    public List<NominatedReview> selectByProjectId(long projectId) {
        return nominatedReviewMapper.selectNominatedReviewByProjectId(projectId);
    }

    public NominatedReview selectAwardReviewByUrlKey(String urlKey){
        return nominatedReviewMapper.selectAwardReviewByUrlKey(urlKey);
    }

    public List<NominatedReview> selectNotAwardReviewByUrlKey(String urlKey){
        return nominatedReviewMapper.selectNotAwardReviewByUrlKey(urlKey);
    }

    public void insertIgnore(List<Long> ids) {
        nominatedReviewMapper.insertIgnore(ids);
    }

    public void updateAwarded(List<Long> ids, boolean awarded) {
        nominatedReviewMapper.updateAwarded(ids, awarded);
    }

    public void deleteByReviewIds(List<Long> ids) {
        nominatedReviewMapper.deleteByReviewIds(ids);
    }

    public List<NominatedReviewCard> selectNominatedReviewCardByProjectId(long projectId) {
        return nominatedReviewMapper.selectNominatedReviewCardByProjectId(projectId);
    }

    public List<NominatedReviewCard> selectAwardedReviewCardByProjectId(long projectId) {
        return nominatedReviewMapper.selectAwardedReviewCardByProjectId(projectId);
    }
}
