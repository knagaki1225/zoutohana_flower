package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReview;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NominatedReviewMapper {
    @Select("""
        SELECT * FROM nominated_reviews
        WHERE project_id = #{projectId}
            AND deleted = false
    """)
    List<NominatedReview> selectNominatedReviewByProjectId(long projectId);

    @Select("""
        SELECT nr.* FROM nominated_reviews nr
        JOIN projects p ON nr.project_id = p.id
        WHERE p.url_key = #{urlKey}
          AND nr.review_awarded = true
          AND nr.deleted = false;
    """)
    NominatedReview selectAwardReviewByUrlKey(String urlKey);

    @Select("""
        SELECT nr.* FROM nominated_reviews nr
        JOIN projects p ON nr.project_id = p.id
        WHERE p.url_key = #{urlKey}
          AND nr.review_awarded = false
          AND nr.deleted = false;
    """)
    List<NominatedReview> selectNotAwardReviewByUrlKey(String urlKey);

    @Update("""
        <script>
            UPDATE nominated_reviews
            SET review_awarded = #{awarded}
            WHERE review_id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">
              #{id}
            </foreach>
        </script>
    """)
    void updateAwarded(@Param("ids") List<Long> ids,
                       @Param("awarded") boolean awarded);

    @Insert("""
    <script>
        INSERT INTO nominated_reviews (
            project_id,
            user_id,
            user_nickname,
            user_address,
            user_age_group,
            user_gender,
            user_self_introduction,
            review_id,
            review_title,
            review_content,
            review_content_edited,
            review_vote_count,
            book_isbn,
            book_title,
            book_publisher,
            book_author,
            created_at,
            updated_at,
            deleted,
            user_icon
        )
        SELECT
            project_id,
            user_id,
            user_nickname,
            user_address,
            user_age_group,
            user_gender,
            user_self_introduction,
            id,
            review_title,
            review_content,
            review_content_edited,
            vote_count,
            book_isbn,
            book_title,
            book_publisher,
            book_author,
            created_at,
            updated_at,
            deleted,
            user_icon
        FROM reviews
        WHERE id IN
        <foreach collection="reviewIds" item="rid" open="(" separator="," close=")">
            #{rid}
        </foreach>
        ON CONFLICT (review_id) DO NOTHING
    </script>
    """)
    void insertIgnore(@Param("reviewIds") List<Long> reviewIds);

    @Delete("""
        <script>
        DELETE FROM nominated_reviews
        WHERE review_id IN
        <foreach collection="ids" item="id" open="(" separator="," close=")">
          #{id}
        </foreach>
        </script>
    """)
    void deleteByReviewIds(@Param("ids") List<Long> ids);

    @Select("""
    SELECT
        nr.*,
        u.login_id AS userLoginId
    FROM nominated_reviews nr
    JOIN users u ON nr.user_id = u.id
    WHERE nr.project_id = #{projectId}
      AND nr.review_awarded = false
        AND nr.deleted = false
    ORDER BY nr.review_vote_count DESC
    """)
    List<NominatedReviewCard> selectNominatedReviewCardByProjectId(long projectId);


    @Select("""
    SELECT
        nr.*,
        u.login_id AS userLoginId
    FROM nominated_reviews nr
    JOIN users u ON nr.user_id = u.id
    WHERE nr.project_id = #{projectId}
        AND nr.review_awarded = true
        AND nr.deleted = false
    ORDER BY nr.review_vote_count DESC
    """)
    List<NominatedReviewCard> selectAwardedReviewCardByProjectId(long projectId);

}
