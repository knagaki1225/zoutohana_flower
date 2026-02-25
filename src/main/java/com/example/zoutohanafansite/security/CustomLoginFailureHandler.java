package com.example.zoutohanafansite.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // ログイン画面の form から送信された 'id' パラメータを取得
        String projectId = request.getParameter("id");

        String redirectUrl = "/login?error";

        // idが存在すれば、パラメータに付け加える
        if (projectId != null && !projectId.isEmpty()) {
            redirectUrl = "/login?id=" + projectId + "&error";
        }

        response.sendRedirect(redirectUrl);
    }
}
