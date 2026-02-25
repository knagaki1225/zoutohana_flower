package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.form.NewPasswordForm;
import com.example.zoutohanafansite.entity.form.PasswordResetForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.service.ProjectService;
import com.example.zoutohanafansite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final UserService userService;
    private final ProjectService projectService;

    public LoginController(UserService userService, ProjectService projectService, ProjectService projectService1) {
        this.userService = userService;
        this.projectService = projectService1;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) Long id) {
        if(id != null){
            Project project = projectService.getProjectById(id);
            if(project == null || project.getStatus() != ProjectStatus.DURING_SUBMISSION){
                throw new IllegalStateException();
            }
        }
        return "auth/login";
    }

    @GetMapping("/password-reset")
    public String passwordReset(Model model, @RequestParam long id) {
        PasswordResetForm passwordResetForm = new PasswordResetForm();
        model.addAttribute("passwordResetForm", passwordResetForm);
        model.addAttribute("id", id);
        return "auth/pw-reset";
    }

    @PostMapping("/password-reset")
    public String passwordReset(@RequestParam long id, PasswordResetForm passwordResetForm, RedirectAttributes redirectAttributes){
        if(userService.getUserByLoginId(passwordResetForm.getLoginId()) == null){
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/password-reset?id="+id;
        }

        if(userService.checkSecurityKey(passwordResetForm)){
            redirectAttributes.addFlashAttribute("alreadyCheck", true);
            User user = userService.getUserByLoginId(passwordResetForm.getLoginId());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/password-reset/input-password?id="+id;
        }
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:/password-reset?id="+id;
    }

    @GetMapping("/password-reset/input-password")
    public String inputResetPassword(@RequestParam long id, @ModelAttribute User user, Model model){
        if(user == null){
            // エラー
        }
        NewPasswordForm newPasswordForm = new NewPasswordForm();
        newPasswordForm.setLoginId(user.getLoginId());

        model.addAttribute("newPasswordForm", newPasswordForm);
        model.addAttribute("id", id);

        return "/auth/pw-set";
    }

    @PostMapping("/password-reset/input-password")
    public String updatePassword(@RequestParam long id, NewPasswordForm newPasswordForm, RedirectAttributes redirectAttributes){
        String newSecurityKey = userService.passwordReset(newPasswordForm);
        if(newSecurityKey == null){
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/password-reset/input-password?id="+id;
        }

        redirectAttributes.addFlashAttribute("id", id);
        redirectAttributes.addFlashAttribute("securityKey", newSecurityKey);
        return "redirect:/password-reset/security-key?id="+id;
    }

    @GetMapping("/password-reset/security-key")
    public String securityKey(@RequestParam long id, Model model,@ModelAttribute String securityKey){
        model.addAttribute("id", id);
        return "auth/pw-reset-confirm";
    }

    @GetMapping("/admin/login")
    public String adminLogin(){
        return "admin/login";
    }
}
