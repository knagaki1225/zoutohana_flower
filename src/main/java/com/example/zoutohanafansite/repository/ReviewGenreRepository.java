package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.mapper.ReviewGenreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewGenreRepository {

    private final ReviewGenreMapper reviewGenreMapper;

    public void deleteByReviewId(Long reviewId) {
        reviewGenreMapper.deleteByReviewId(reviewId);
    }

    public void insert(Long reviewId, Long genreId) {
        reviewGenreMapper.insert(reviewId, genreId);
    }

    public List<Genre> findGenresByReviewId(Long reviewId) {
        return reviewGenreMapper.selectGenresByReviewId(reviewId);
    }
}
