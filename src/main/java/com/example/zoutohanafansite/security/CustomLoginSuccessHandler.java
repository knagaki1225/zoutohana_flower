package com.example.zoutohanafansite.security;

import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.service.ProjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.zoutohanafansite.entity.enums.ProjectStatus.DURING_SUBMISSION;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ProjectService projectService;

    public CustomLoginSuccessHandler(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String projectId = request.getParameter("id");

        if( projectId.isEmpty()){
            response.sendRedirect("/mypage");
            return;
        }

        Project project = projectService.getProjectById(Long.parseLong(projectId));

        if(project == null || !project.getStatus().equals(DURING_SUBMISSION)){
            // throw exception 不正なprojectId (マイページへリダイレクト)
        }else{
            response.sendRedirect("/review?id=" + projectId);
        }
    }
}
