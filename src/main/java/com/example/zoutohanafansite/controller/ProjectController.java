package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.admin.project.ProjectCard;
import com.example.zoutohanafansite.entity.nominatedreview.NominatedReviewWork;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.exception.ProjectNotFoundException;
import com.example.zoutohanafansite.service.NominatedReviewService;
import com.example.zoutohanafansite.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final NominatedReviewService nominatedReviewService;

    public ProjectController(ProjectService projectService, NominatedReviewService nominatedReviewService) {
        this.projectService = projectService;
        this.nominatedReviewService = nominatedReviewService;
    }

    @GetMapping("/{urlKey}")
    public String project(@PathVariable("urlKey") String urlKey, Model model) {
        Project project = projectService.getProjectByUrlKey(urlKey);
        if (project == null) {
            throw new ProjectNotFoundException();
        }

        String returnUrl = "project/first-ex";

        switch (project.getStatus()){
            case BEFORE_SUBMISSION :
            case DURING_SUBMISSION :
            case FIRST_PHASE:
            case SECOND_PHASE_VERIFY:
                break;
            case SECOND_PHASE_VOTING:
                returnUrl = "project/first-review";
                break;
            case SECOND_PHASE_RESULT:
                returnUrl = "project/nominate-works";

                List<NominatedReviewWork> reviews = nominatedReviewService.getNominatedReviewsByProjectId(project.getId());
                model.addAttribute("reviews", reviews);
                break;
            case AWARD_ANNOUNCEMENT:
                returnUrl = "project/nominate-award";
                break;
        }

        model.addAttribute("project", project);

        return returnUrl;
    }

//    @GetMapping("/allOngoingProject")
//    public String allOngoingAdmin(Model model) {
//        List<ProjectCard> projects = projectService.getAllOngoingProjectsAdmin();
//        model.addAttribute("projects", projects);
//
//        return "admin/top";
//    }

    @PostMapping("/delete")
    public String projectDelete(@RequestParam Long projectId,
                                @RequestParam String urlKey,
                                HttpServletRequest request) {
        Project targetProject = projectService.getProjectById(projectId);
        String referer = request.getHeader("Referer");

        if(targetProject.getUrlKey().equals(urlKey)) {  /* 確認入力が正しいなら削除させる */
            projectService.deleteProjectById(projectId);

            if (referer == ("admin/project_edit/" +  urlKey)) {
                return "redirect:admin/project_list";
            }
        }

        return "redirect:" + referer;
    }
}
