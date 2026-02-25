package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.ProjectSearchForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProjectRepository {
    private final ProjectMapper projectMapper;

    public ProjectRepository(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public Project getProjectById(long id) {
        return projectMapper.getProjectById(id);
    }

    public Project getProjectByUrlKey(String urlKey){
        return projectMapper.getProjectByUrlKey(urlKey);
    }

    public Project getAllProjectByUrlKey(String urlKey){
        return projectMapper.getAllProjectByUrlKey(urlKey);
    }

    public List<ProjectCard> getAllProjects(ProjectSearchForm form){
        return projectMapper.getAllProjects(form);
    }

    public List<Project> getAllOngoingProjects(){
        return projectMapper.getAllOngoingProjects();
    }

    public List<ProjectCard> getAllOngoingProjectsAdmin(){
        return projectMapper.getAllOngoingProjectsAdmin();
    }

    public List<Project> getAllPastProjects(){
        return projectMapper.getAllPastProjects();
    }

    public boolean deleteProjectById(long id) {
        return projectMapper.deleteProjectById(id);
    }

    public LocalDateTime selectVotingEndAt(String urlKey){
        return projectMapper.selectVotingEndAt(urlKey);
    }

    public boolean updateProject(Project project) {
        return projectMapper.updateProject(project);
    }

    public void createProject(Project project) {
        projectMapper.createProject(project);
    }

    public void updateImageUrl(String urlText, long id){
        projectMapper.updateImageUrl(urlText, id);
    }

    public void updateStatus(ProjectStatus status, String urlKey){
        projectMapper.updateStatus(status, urlKey);
    }
}
