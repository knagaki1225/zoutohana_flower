package com.example.zoutohanafansite.controller;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.enums.ProjectStatus;
import com.example.zoutohanafansite.entity.enums.UserStatus;
import com.example.zoutohanafansite.entity.form.SignupForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.validator.SignupValidator;
import com.example.zoutohanafansite.exception.AccessDeniedException;
import com.example.zoutohanafansite.service.GenerateSecurityKeyService;
import com.example.zoutohanafansite.service.ProjectService;
import com.example.zoutohanafansite.service.UserService;
import com.example.zoutohanafansite.service.ValidationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final GenerateSecurityKeyService generateSecurityKeyService;
    private final UserService userService;
    private final ProjectService projectService;

    public SignupController(PasswordEncoder passwordEncoder, ValidationService validationService, GenerateSecurityKeyService generateSecurityKeyService, UserService userService, ProjectService projectService) {
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
        this.generateSecurityKeyService = generateSecurityKeyService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping
    public String signup(Model model, @RequestParam long id) {
        Project project = projectService.getProjectById(id);
        if(!project.getStatus().equals(ProjectStatus.DURING_SUBMISSION)){
            throw new AccessDeniedException();
        }

        SignupForm signupForm = new SignupForm();
        signupForm.setIcon(1);

        SignupValidator signupValidator = new SignupValidator();
        model.addAttribute("signupValidator", signupValidator);
        model.addAttribute("signupForm", signupForm);
        model.addAttribute("id", id);
        return "auth/signup";
    }

    @GetMapping("/rewrite")
    public String rewriteSignup(Model model, @RequestParam long id,@ModelAttribute SignupForm signupForm, @ModelAttribute SignupValidator signupValidator) {
        Project project = projectService.getProjectById(id);
        if(!project.getStatus().equals(ProjectStatus.DURING_SUBMISSION)){
            throw new AccessDeniedException();
        }
        model.addAttribute("id", id);
        return "auth/signup";
    }

    @PostMapping("/rewrite")
    public String rewrite(@RequestParam long id, RedirectAttributes redirectAttributes, SignupForm signupForm) {
        redirectAttributes.addFlashAttribute("signupForm", signupForm);
        return "redirect:/signup/rewrite?id=" + id;
    }

    @PostMapping
    public String insertUser(@ModelAttribute("signupForm") SignupForm signupForm, RedirectAttributes redirectAttributes, @RequestParam long id) {
        SignupValidator signupValidator = validationService.validateSignUp(signupForm);

        if(signupValidator.isValid()) {
            redirectAttributes.addFlashAttribute("signupForm", signupForm);
            redirectAttributes.addFlashAttribute("signupValidator", signupValidator);
            return "redirect:/signup/rewrite?id=" + id;
        }

        String hashedPW = passwordEncoder.encode(signupForm.getPassword());
        signupForm.setPassword(hashedPW);
        redirectAttributes.addFlashAttribute("signupForm", signupForm);
        return  "redirect:/signup/confirm?id=" + id;
    }

    @GetMapping("/confirm")
    public String confirm(Model model, @RequestParam long id, SignupForm signupForm) {
        model.addAttribute("signupForm", signupForm);
        model.addAttribute("id", id);
        return "auth/signup-confirm";
    }

    @PostMapping("/confirm")
    public String register(SignupForm signupForm, @RequestParam long id,RedirectAttributes redirectAttributes) {
        User newUser = new User();
        String securityKey = GenerateSecurityKeyService.generateSecurityKey();
        String hashedSecurityKey = passwordEncoder.encode(securityKey);

        newUser.setLoginId(signupForm.getLoginId());
        newUser.setNickname(signupForm.getNickname());
        newUser.setPassword(signupForm.getPassword());
        newUser.setSelfIntroduction(signupForm.getSelfIntroduction());
        newUser.setIcon(signupForm.getIcon());
        newUser.setAddress(signupForm.getAddress());
        newUser.setBirthYear(signupForm.getBirthYear());
        newUser.setGender(signupForm.getUserGender());
        newUser.setSecurityKey(hashedSecurityKey);
        newUser.setStatus(UserStatus.ACTIVE);

        userService.insertUser(newUser);

        redirectAttributes.addFlashAttribute("securityKey", securityKey);
        return "redirect:/signup/security-key?id=" + id;
    }

    @GetMapping("/security-key")
    public String securityKey(@RequestParam long id, Model model){
        model.addAttribute("id", id);
        return "auth/security";
    }
}
