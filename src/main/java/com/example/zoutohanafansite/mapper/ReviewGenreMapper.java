package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.genre.Genre;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewGenreMapper {

    @Delete("DELETE FROM review_genres WHERE review_id = #{reviewId}")
    void deleteByReviewId(Long reviewId);

    @Insert("""
        INSERT INTO review_genres (review_id, genre_id, created_at, updated_at, deleted)
        VALUES (#{reviewId}, #{genreId}, NOW(), NOW(), false)
    """)
    void insert(@Param("reviewId") Long reviewId,
                @Param("genreId") Long genreId);

    @Select("""
        SELECT g.id, g.name, g.furigana
        FROM review_genres rg
        JOIN genres g ON rg.genre_id = g.id
        WHERE rg.review_id = #{reviewId}
          AND rg.deleted = false
          AND g.deleted = false
    """)
    List<Genre> selectGenresByReviewId(Long reviewId);
}
