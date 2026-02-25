package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.AdminProjectCreateForm;
import com.example.zoutohanafansite.entity.form.ProjectSearchForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * 企画を作成
     *
     * @param form AdminProjectCreateForm
     * @return project 作成した企画
     */
    public Project createProject(AdminProjectCreateForm form) {
        Project project = new Project();
        project.setName(form.getName());
        project.setUrlKey(form.getUrlKey());
        project.setIntroduction(form.getIntroduction());
        project.setThemeColor(form.getThemeColor());
        project.setProjectStartAt(form.getProjectStartAt());
        project.setProjectEndAt(form.getProjectEndAt());
        project.setSubmissionStartAt(form.getSubmissionStartAt());
        project.setSubmissionEndAt(form.getSubmissionEndAt());
        project.setVotingStartAt(form.getVotingStartAt());
        project.setVotingEndAt(form.getVotingEndAt());
        project.setMainImgUrl(null);
        project.setStatus(ProjectStatus.valueOf("BEFORE_SUBMISSION"));
        project.setPublished(false);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.createProject(project);
        return project;
    }

    /**
     * 指定したidのProjectを取得
     *
     * @param id     取得するprojectのid
     *
     * @return Project
     */
    public Project getProjectById(long id) {
        return projectRepository.getProjectById(id);
    }

    /**
     * urlKeyを指定してProjectを取得
     *
     * @param urlKey 指定するurlKey
     * @return Project
     */
    public Project getProjectByUrlKey(String urlKey){
        return projectRepository.getProjectByUrlKey(urlKey);
    }

    /**
     * urlKeyを指定して非公開でもProjectを取得
     *
     * @param urlKey 指定するurlKey
     * @return Project
     */
    public Project getAllProjectByUrlKey(String urlKey){
        return projectRepository.getAllProjectByUrlKey(urlKey);
    }

    /**
     * Projectを全件取得(管理者用)
     *
     * @param form ProjectSearchForm(検索条件)
     *              String sort, keyword
     *              List<String> status, published
     *              List<LocalDateTime> startAt, endAt
     * @return List<ProjectCard>
     */
    public List<ProjectCard> getAllProjects(ProjectSearchForm form){
        return projectRepository.getAllProjects(form);
    }

    /**
     * 開催中のProjectを全件取得
     *
     * @return List<Project>
     */
    public List<Project> getAllOngoingProjects(){
        return projectRepository.getAllOngoingProjects();
    }

    /**
     * 開催中のProjectを全件取得(管理者画面の企画カード用)
     *
     * @return List<ProjectCard>
     */
    public List<ProjectCard> getAllOngoingProjectsAdmin(){
        return projectRepository.getAllOngoingProjectsAdmin();
    }

    /**
     * すべてのProjectを取得
     *
     * @return List<Project>
     */
    public List<Project> getAllPastProjects(){
        return projectRepository.getAllPastProjects();
    }

    /**
     * 次のステータスまであと何日か取得
     *
     * @param project 情報が欲しいProject
     * @return List<Project>
     */
    public Integer getLastDate(Project project){
        LocalDateTime targetDate;
        LocalDateTime now = LocalDateTime.now();
        switch (project.getStatus()){
            case BEFORE_SUBMISSION:
                targetDate = project.getSubmissionStartAt();
                break;
            case DURING_SUBMISSION:
                targetDate = project.getSubmissionEndAt();
                break;
            case SECOND_PHASE_VOTING:
                targetDate = project.getVotingEndAt();
                break;
            default:
                return null;
        }
        return (int) ChronoUnit.DAYS.between(now.toLocalDate(), targetDate.toLocalDate());
    }

    /**
     * 指定したidのProjectを削除
     *
     * @param id     削除するprojectのid
     *
     * @return boolean
     */
    public boolean deleteProjectById(long id) {
        return projectRepository.deleteProjectById(id);
    }

    /**
     * 指定したurlKeyのProjectの投票終了日を取得
     *
     * @param urlKey 指定するurlKey
     *
     * @return LocalDateTime
     */
    public LocalDateTime getVotingEndAt(String urlKey){
        return projectRepository.selectVotingEndAt(urlKey);
    }

    /**
     * 指定したidのProjectの情報を更新
     *
     * @param project 上書きするプロジェクトの内容
     *
     * @return boolean
     */
    public boolean updateProject(Project project){
        return projectRepository.updateProject(project);
    }

    public void updateImageUrl(String urlText, long id){
        projectRepository.updateImageUrl(urlText, id);
    }

    public void updateStatus(ProjectStatus status, String urlKey){
        projectRepository.updateStatus(status, urlKey);
    }

    public void nextStatus(String urlKey){
        Project project = getProjectByUrlKey(urlKey);
        ProjectStatus projectStatus = project.getStatus();
        switch (project.getStatus()){
            case BEFORE_SUBMISSION:
                projectStatus = ProjectStatus.DURING_SUBMISSION;
                break;
            case DURING_SUBMISSION:
                projectStatus = ProjectStatus.FIRST_PHASE;
                break;
            case FIRST_PHASE:
                projectStatus = ProjectStatus.SECOND_PHASE_VOTING;
            case SECOND_PHASE_VOTING:
                projectStatus = ProjectStatus.SECOND_PHASE_VERIFY;
                break;
            case SECOND_PHASE_VERIFY:
                projectStatus = ProjectStatus.SECOND_PHASE_RESULT;
                break;
            case SECOND_PHASE_RESULT:
                projectStatus = ProjectStatus.AWARD_ANNOUNCEMENT;
        }
        updateStatus(projectStatus, urlKey);
    }
}
