package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.post.PostPagination;
import com.example.zoutohanafansite.entity.post.PostTop;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.project.ProjectTop;
import com.example.zoutohanafansite.service.PostService;
import com.example.zoutohanafansite.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TopController {
    private final PostService postService;
    private final ProjectService projectService;

    public TopController(PostService postService, ProjectService projectService) {
        this.postService = postService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String index(Model model){
        List<Post> posts = postService.getPublicPosts().stream().limit(5).toList();
        List<PostTop> postTops = new ArrayList<>();
        for(Post post : posts){
            postTops.add(new PostTop(post));
        }
        model.addAttribute("posts", postTops);

        List<Project> ongoingProjects = projectService.getAllOngoingProjects();
        List<ProjectTop> ongoingProjectTops = new ArrayList<>();
        for(Project project : ongoingProjects){
            ongoingProjectTops.add(new ProjectTop(project.getId(), project.getName(), project.getUrlKey(), project.getIntroduction(), project.getMainImgUrl(), project.getThemeColor(), project.getProjectStartAt(), project.getProjectEndAt(), project.getStatus()));
        }

        model.addAttribute("ongoingProjects", ongoingProjectTops);

        List<Project> pastProjects = projectService.getAllPastProjects();
        List<ProjectTop> pastProjectTops = new ArrayList<>();
        for(Project project : pastProjects){
            pastProjectTops.add(new ProjectTop(project.getId(), project.getName(), project.getUrlKey(), project.getIntroduction(), project.getMainImgUrl(), project.getThemeColor(), project.getProjectStartAt(), project.getProjectEndAt(), project.getStatus()));
        }

        model.addAttribute("pastProjects", pastProjectTops);

        return "fansite/top";
    }

    @GetMapping("/news")
    public String news(Model model, @RequestParam(defaultValue = "new") String category, @RequestParam(defaultValue = "1") int page){
        String categoryName = "";
        switch (category){
            case "new":
                categoryName = "新着情報";
                break;
            case "project":
                categoryName = "企画情報";
                break;
            case "donation":
                categoryName = "寄贈情報";
                break;
            case "else":
                categoryName = "その他情報";
                break;
            default:
                categoryName = "新着情報";
                category = "new";
        }
        model.addAttribute("category", categoryName);

        PostPagination postPagination = postService.getPostPagination(category, page);
        model.addAttribute("postPagination", postPagination);
        return "info/post-list";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(Model model, @PathVariable long id){
        Post post = postService.getPublicPostById(id);
        model.addAttribute("post", post);
        return "info/post-detail";
    }

    @GetMapping("/news/search")
    public String newsSearch(Model model, @RequestParam String s){
        List<PostTop> postTops = postService.getPostTopsByKeyword(s);
        PostPagination postPagination = new PostPagination(null, postTops);
        model.addAttribute("postPagination", postPagination);

        model.addAttribute("category", "検索");

        return "info/post-list";
    }

    @GetMapping("/projects")
    public String project(Model model, @RequestParam(defaultValue = "ongoing") String s){
        List<Project> projects;
        Boolean status = false;
        if(s.equals("past")){
            projects = projectService.getAllPastProjects();
        }else{
            projects = projectService.getAllOngoingProjects();
            status = true;
        }

        List<ProjectTop> projectTops = new ArrayList<>();

        for(Project project : projects){
            projectTops.add(new ProjectTop(project.getId(), project.getName(), project.getUrlKey(), project.getIntroduction(), project.getMainImgUrl(), project.getThemeColor(), project.getProjectStartAt(), project.getProjectEndAt(), project.getStatus()));
        }

        model.addAttribute("projects", projectTops);
        model.addAttribute("status", status);
        return "fansite/project-list";
    }
}
