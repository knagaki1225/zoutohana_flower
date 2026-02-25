package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.ProjectSearchForm;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.project.Project;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProjectMapper {

    @Select("SELECT * FROM projects WHERE id = #{id}")
    Project getProjectById(long id);

    // src/main/resources/mapper/ProjectMapper.xml
    List<ProjectCard> getAllProjects(ProjectSearchForm form);

    @Select("""
            SELECT * FROM projects
                WHERE url_key = #{urlKey}
                    AND deleted = false
    """)
    Project getProjectByUrlKey(String urlKey);


    @Select("""
            SELECT * FROM projects
                WHERE url_key = #{urlKey}
                    AND deleted = false
    """)
    Project getAllProjectByUrlKey(String urlKey);
            
// src/main/resources/mapper/ProjectMapper.xml
//    List<ProjectCard> getAllProjects(ProjectSearchForm form);

    @Select("""
            SELECT * FROM projects
                WHERE published = true
                    AND status <> 'AWARD_ANNOUNCEMENT'
                    AND deleted = false
    """)
    List<Project> getAllOngoingProjects();

    @Select("""
            SELECT
                p.*,
                COUNT(r.id) AS review_count
            FROM projects p
            LEFT JOIN reviews r
                ON r.project_id = p.id
            WHERE p.published = true
                AND p.status <> 'AWARD_ANNOUNCEMENT'
                AND p.deleted = false
            GROUP BY p.id
    """)
    List<ProjectCard> getAllOngoingProjectsAdmin();

    @Select("""
            SELECT * FROM projects
                WHERE published = true
                    AND status = 'AWARD_ANNOUNCEMENT'
                    AND deleted = false
    """)
    List<Project> getAllPastProjects();

    @Update("""
        UPDATE projects
        SET deleted = true
        WHERE id = #{id}
    """)
    boolean deleteProjectById(long id);

    @Select("""
        SELECT voting_end_at from projects WHERE url_key = #{urlKey} AND deleted = false
    """)
    LocalDateTime selectVotingEndAt(String urlKey);

    @Update("""
        UPDATE projects
        SET
            published = #{project.published},
            status = #{project.status},
            name = #{project.name},
            url_key = #{project.urlKey},
            introduction = #{project.introduction},
            project_start_at = #{project.projectStartAt},
            project_end_at = #{project.projectEndAt},
            submission_start_at = #{project.submissionStartAt},
            submission_end_at = #{project.submissionEndAt},
            voting_start_at = #{project.votingStartAt},
            voting_end_at = #{project.votingEndAt},
            enable_visible_book_title = #{project.enableVisibleBookTitle},
            enable_visible_review_title = #{project.enableVisibleReviewTitle},
            enable_visible_user_info = #{project.enableVisibleUserInfo},
            theme_color = #{project.themeColor},
            main_img_url = #{project.mainImgUrl},
            updated_at = NOW()
        WHERE id = #{project.id}
    """)
    boolean updateProject(@Param("project") Project project);

    @Insert("""
        INSERT INTO projects (
            name,
            url_key,
            introduction,
            main_img_url,
            theme_color,
            status,
            enable_visible_book_title,
            enable_visible_review_title,
            enable_visible_user_info,
            published,
            project_start_at,
            project_end_at,
            submission_start_at,
            submission_end_at,
            voting_start_at,
            voting_end_at,
            created_at,
            updated_at
        ) VALUES (
            #{name},
            #{urlKey},
            #{introduction},
            #{mainImgUrl},
            #{themeColor},
            #{status},
            #{enableVisibleBookTitle},
            #{enableVisibleReviewTitle},
            #{enableVisibleUserInfo},
            #{published},
            #{projectStartAt},
            #{projectEndAt},
            #{submissionStartAt},
            #{submissionEndAt},
            #{votingStartAt},
            #{votingEndAt},
            #{createdAt},
            #{updatedAt}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createProject(Project project);

    @Update("""
        UPDATE projects
        SET main_img_url = #{urlText}
        WHERE id = #{id}
    """)
    void updateImageUrl(String urlText, long id);

    @Update("""
        UPDATE projects
        SET status = #{status}
        WHERE url_key = #{urlKey}
    """)
    void updateStatus(ProjectStatus status, String urlKey);
}
