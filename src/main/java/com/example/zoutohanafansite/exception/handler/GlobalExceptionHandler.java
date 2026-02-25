package com.example.zoutohanafansite.exception.handler;

import com.example.zoutohanafansite.entity.auth.ErrorStatus;
import com.example.zoutohanafansite.exception.AccessDeniedException;
import com.example.zoutohanafansite.exception.AdminUserNotFoundException;
import com.example.zoutohanafansite.exception.IllegalStateException;
import com.example.zoutohanafansite.exception.ProjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String exception(Exception ex, RedirectAttributes redirectAttributes){
        String status ="EXCEPTION";
        String message = "エラーが発生しました。";
        String redirectPath = "redirect:/error-4xx";

        // ResponseStatusException（明示的な例外）の判定
        if (ex instanceof ResponseStatusException rse) {
            HttpStatusCode code = rse.getStatusCode();

            if (code.equals(HttpStatus.NOT_FOUND)) { // 404
                status = "NOT FOUND";
                message = "ページが見つかりません。";
            } else if (code.equals(HttpStatus.FORBIDDEN)) { // 403
                status = "FORBIDDEN";
                message = "権限がありません。ログインをしてください。";
            } else if (code.is4xxClientError()) { // その他400系
                message = "エラーが発生しました。";
                status = "EXCEPTION";
            }
        }

        // 自作クラスにセットしてフラッシュ属性で渡す
        ErrorStatus errorStatus = new ErrorStatus(status, message);
        redirectAttributes.addFlashAttribute("errorStatus", errorStatus);

        return redirectPath;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ErrorStatus errorStatus = new ErrorStatus("FORBIDDEN", "アクセスする権限がありません");
        redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
        return "redirect:/error-4xx";
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public String handleProjectNotFoundException(ProjectNotFoundException ex, RedirectAttributes redirectAttributes){
        ErrorStatus errorStatus = new ErrorStatus("NOT FOUND", "お探しのページは見つかりません");
        return "redirect:/error-4xx";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, RedirectAttributes redirectAttributes){
        ErrorStatus errorStatus = new ErrorStatus("FORBIDDEN", "エラーが発生しました。最初からやり直してください。");
        redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
        return "redirect:/error-4xx";
    }

    @ExceptionHandler(AdminUserNotFoundException.class)
    public String handleAdminUserNotFound(AdminUserNotFoundException ex){
        return "redirect:/admin/login?error";
    }
}
