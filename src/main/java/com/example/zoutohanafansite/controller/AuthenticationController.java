package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.form.AccountEditForm;
import com.example.zoutohanafansite.entity.form.NewPasswordForm;
import com.example.zoutohanafansite.entity.form.PasswordResetForm;
import com.example.zoutohanafansite.security.CustomUserDetails;
import com.example.zoutohanafansite.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/leave")
    public String accountLeave(){
        return "auth/delete-account";
    }

    @PostMapping("/leave")
    public String accountDelete(@AuthenticationPrincipal CustomUserDetails user, HttpServletRequest request, HttpServletResponse response, Authentication authentication, RedirectAttributes redirectAttributes){
        userService.deleteUser(user.getUserId());

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        redirectAttributes.addFlashAttribute("isDeletedSuccessfully", true);

        return "redirect:/auth/leave/complete";
    }

    @GetMapping("/leave/complete")
    public String accountDeleteComplete(Model model){
        if(!model.containsAttribute("isDeletedSuccessfully")){
            // 不正アクセス
        }

        return "auth/delete-account-complete";
    }

    @GetMapping("/edit")
    public String accountEdit(Model model, @AuthenticationPrincipal CustomUserDetails customUser){
        User user = userService.getUserByLoginId(customUser.getUsername());

        AccountEditForm accountEditForm = new AccountEditForm();
        accountEditForm.setNickname(user.getNickname());
        accountEditForm.setIcon(user.getIcon());
        accountEditForm.setAddress(user.getAddress());
        accountEditForm.setBirthYear(user.getBirthYear());
        accountEditForm.setUserGender(user.getGender());
        accountEditForm.setSelfIntroduction(user.getSelfIntroduction());

        model.addAttribute("accountEditForm", accountEditForm);
        model.addAttribute("loginId", user.getLoginId());
        return "auth/edit-account";
    }

    @PostMapping("/edit")
    public String accountUpdate(AccountEditForm form, @AuthenticationPrincipal CustomUserDetails customUser){
        User user = userService.getUserByLoginId(customUser.getUsername());

        user.setNickname(form.getNickname());
        user.setIcon(form.getIcon());
        user.setAddress(form.getAddress());
        user.setBirthYear(form.getBirthYear());
        user.setSelfIntroduction(form.getSelfIntroduction());

        userService.updateUser(user);

        return "redirect:/mypage";

    }
}
