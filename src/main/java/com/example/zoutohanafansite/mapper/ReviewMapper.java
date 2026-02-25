package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.admin.review.NominatedReviewCard;
import com.example.zoutohanafansite.entity.admin.review.ReviewList;
import com.example.zoutohanafansite.entity.form.ProjectSearchForm;
import com.example.zoutohanafansite.entity.form.ReviewForm;
import com.example.zoutohanafansite.entity.form.ReviewSearchForm;
import com.example.zoutohanafansite.entity.review.Review;
import com.example.zoutohanafansite.entity.admin.review.ReviewCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {
    @Insert("""
        INSERT INTO Reviews (
            project_id,
            user_id,
            user_nickname,
            user_address,
            user_age_group,
            user_gender,
            user_self_introduction,
            book_isbn,
            book_title,
            book_publisher,
            book_author,
            review_title,
            review_content,
            draft
        ) VALUES (
            #{projectId},
            #{userId},
            #{userNickname},
            #{userAddress},
            #{userAgeGroup},
            #{userGender},
            #{userSelfIntroduction},
            #{bookIsbn},
            #{bookTitle},
            #{bookPublisher},
            #{bookAuthor},
            #{reviewTitle},
            #{reviewContent},
            #{draft}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id,createdAt,updatedAt", keyColumn = "id,created_at,updated_at")
    void insertReview(Review review);

    @Select("SELECT * FROM reviews WHERE id = #{id} AND deleted = false")
    Review selectReviewById(long id);

    // src/main/resources/mapper/ReviewMapper.xml
    List<ReviewList> getReviewsByUrlKey(ReviewSearchForm form, String urlKey);

    @Update("""
    UPDATE reviews
        SET
            project_id = #{projectId},
            user_id = #{userId},
            user_nickname = #{userNickname},
            user_address = #{userAddress},
            user_age_group = #{userAgeGroup},
            user_gender = #{userGender},
            user_self_introduction = #{userSelfIntroduction},
            book_isbn = #{bookIsbn},
            book_title = #{bookTitle},
            book_publisher = #{bookPublisher},
            book_author = #{bookAuthor},
            review_title = #{reviewTitle},
            review_content = #{reviewContent},
            draft = #{draft},
            updated_at = CURRENT_TIMESTAMP
    WHERE id = #{id};
    """)
    void updateDraftReview(Review review);

    @Update("UPDATE reviews SET deleted = true WHERE id = #{id}")
    void deleteReviewById(long id);

    @Select("SELECT * FROM reviews WHERE user_id = #{userId}")
    List<Review> selectReviewsByUserId(long userId);

    @Select("""
        SELECT
            r.*,
            p.url_key AS project_url_key,
            p.name AS project_name,
            u.login_id AS user_login_id,
            CASE
                WHEN r.first_stage_passed = FALSE THEN 'INITIAL'
                WHEN r.first_stage_passed = TRUE AND nr.id IS NULL THEN 'FIRST_STAGE_PASSED'
                WHEN nr.id IS NOT NULL AND nr.review_awarded = FALSE THEN 'SECOND_STAGE_PASSED'
                WHEN nr.review_awarded = TRUE THEN 'AWARDED'
                ELSE 'UNKNOWN'
            END AS status
        FROM reviews r
        JOIN projects p ON r.project_id = p.id
        LEFT JOIN users u ON r.user_id = u.id
        LEFT JOIN nominated_reviews nr ON r.id = nr.review_id
        WHERE r.id = #{id}
            AND r.deleted = FALSE
            AND r.draft = FALSE
    """)
    ReviewCard selectReviewCardById(long id);

    @Select("""
        SELECT
            r.*,
            p.url_key AS project_url_key,
            p.name AS project_name,
            u.login_id AS user_login_id,
            CASE
                WHEN r.first_stage_passed = FALSE THEN 'INITIAL'
                WHEN r.first_stage_passed = TRUE AND nr.id IS NULL THEN 'FIRST_STAGE_PASSED'
                WHEN nr.id IS NOT NULL AND nr.review_awarded = FALSE THEN 'SECOND_STAGE_PASSED'
                WHEN nr.review_awarded = TRUE THEN 'AWARDED'
                ELSE 'UNKNOWN'
            END AS status
        FROM reviews r
        JOIN projects p ON r.project_id = p.id
        LEFT JOIN users u ON r.user_id = u.id
        LEFT JOIN nominated_reviews nr ON r.id = nr.review_id
        WHERE r.user_id = #{userId}
            AND r.deleted = FALSE
            AND r.draft = FALSE
    """)
    List<ReviewCard> selectReviewCardsByUserId(long userId);

    @Select("""
            SELECT id FROM reviews 
            WHERE project_id = #{projectId}
              AND user_id = #{userId}
              AND draft = true
              AND deleted = false
    """)
    Long selectDraftId(long projectId, long userId);

    @Select("""
            SELECT * FROM reviews
            WHERE user_id = #{userId}
              AND draft = false
              AND deleted = false
    """)
    List<Review> selectAllReviewsNotDraftByUserId(long userId);

    @Update("""
    UPDATE reviews
        SET
            review_title = #{reviewForm.reviewTitle},
            review_content = #{reviewForm.reviewContent},
            updated_at = CURRENT_TIMESTAMP
    WHERE id = #{id};
    """)
    void updateReview(ReviewForm reviewForm, long id);

    @Select("""
        SELECT r.*
        FROM reviews r 
        JOIN projects p ON r.project_id = p.id
        WHERE p.url_key = #{urlKey}
        AND r.first_stage_passed = true
        ORDER BY r.vote_count DESC
    """)
    List<Review> selectReviewsByUrlKey(String urlKey);

    @Update("""
        UPDATE reviews
        SET vote_count = vote_count + 1
        WHERE id = #{id}
    """)
    void incrementVoteCount(long id);

    @Update("""
        UPDATE reviews
        SET vote_count = vote_count - 1
        WHERE id = #{id}
          AND vote_count > 0
    """)
    void decrementVoteCount(long id);

    @Select("""
        <script>
            SELECT r.* FROM reviews r
            INNER JOIN projects p ON r.project_id = p.id
            WHERE p.url_key = #{urlKey}
                AND r.id IN
                    <foreach item="id" collection="idList" open="(" separator="," close=")">
                        #{id}
                    </foreach>
        </script>
    """)
    List<Review> selectReviewByUrlKeyAndIdList(String urlKey, List<Long> idList);

    @Update("""
        <script>
            UPDATE reviews
            SET first_stage_passed = #{passed}
            WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">
                #{id}
            </foreach>
        </script>
    """)
    void updateFirstStagePassed(@Param("ids") List<Long> ids,
                                @Param("passed") boolean passed);

    @Select("""
    SELECT
        r.*,
        u.login_id AS userLoginId,
        CASE
            WHEN r.first_stage_passed = FALSE THEN '一次審査未通過'
            WHEN r.first_stage_passed = TRUE AND nr.id IS NULL THEN '一次審査通過'
            WHEN nr.id IS NOT NULL AND nr.review_awarded = FALSE THEN 'ノミネート'
            WHEN nr.review_awarded = TRUE THEN '大賞'
            ELSE 'UNKNOWN'
        END AS status
    FROM reviews r
    JOIN projects p ON r.project_id = p.id
    LEFT JOIN nominated_reviews nr ON r.id = nr.review_id
    LEFT JOIN users u ON r.user_id = u.id
    WHERE p.url_key = #{urlKey}
        AND r.deleted = FALSE
        AND r.draft = FALSE
    ORDER BY
        CASE
        WHEN r.first_stage_passed = FALSE THEN 1
        WHEN r.first_stage_passed = TRUE AND nr.id IS NULL THEN 2
        WHEN nr.id IS NOT NULL AND nr.review_awarded = FALSE THEN 3
        WHEN nr.review_awarded = TRUE THEN 4
        ELSE 5
        END DESC
    """)
    List<ReviewCard> selectReviewCardForExport(String urlKey);
}
