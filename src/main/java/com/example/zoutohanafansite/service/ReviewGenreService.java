package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.repository.ReviewGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewGenreService {

    private final ReviewGenreRepository reviewGenreRepository;

    @Transactional
    public void updateReviewGenres(Long reviewId, List<Long> genreIds) {
        // 既存削除
        reviewGenreRepository.deleteByReviewId(reviewId);

        // 再登録
        for (Long genreId : genreIds) {
            reviewGenreRepository.insert(reviewId, genreId);
        }
    }

    public List<Genre> getGenresByReviewId(Long reviewId) {
        return reviewGenreRepository.findGenresByReviewId(reviewId);
    }
}
