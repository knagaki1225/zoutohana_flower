package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.form.AdminProjectCreateForm;
import com.example.zoutohanafansite.entity.form.SignupForm;
import com.example.zoutohanafansite.entity.project.Project;
import com.example.zoutohanafansite.entity.validator.ProjectValidator;
import com.example.zoutohanafansite.entity.validator.SignupValidator;
import com.example.zoutohanafansite.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public ValidationService(UserRepository userRepository, ProjectService projectService) {
        this.userRepository = userRepository;
        this.projectService = projectService;
    }


    private boolean isNull(Object o){
        return o == null;
    }

    public SignupValidator validateSignUp(SignupForm signupForm) {
        SignupValidator signupValidator = new SignupValidator();

        if(signupForm.getLoginId().isEmpty()){
            signupValidator.setNullLoginId(true);
        }else{
            if(userRepository.getUserByLoginId(signupForm.getLoginId()) != null){
                signupValidator.setExistsLoginId(true);
            }
        }
        signupValidator.setNullNickName(signupForm.getNickname().isEmpty());
        signupValidator.setNullAddress(signupForm.getAddress().isEmpty());
        signupValidator.setNullBirthYear(isNull(signupForm.getBirthYear()));
        signupValidator.setNullPassword(signupForm.getPassword().isEmpty());
        signupValidator.setNullConfirmPassword(signupForm.getConfirmPassword().isEmpty());

        if(!signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            signupValidator.setPasswordMismatch(true);
        }

        signupValidator.updateStatus();

        return signupValidator;
    }

    public ProjectValidator validateProject(AdminProjectCreateForm form) {
        ProjectValidator projectValidator = new ProjectValidator();

        projectValidator.setNullName(form.getName().isEmpty());

        if(form.getUrlKey().isEmpty()){
            projectValidator.setNullUrlKey(true);
        }else{
            if(projectService.getProjectByUrlKey(form.getUrlKey()) != null){
                projectValidator.setExistsUrlKey(true);
            }
        }

        projectValidator.setNullIntroduction(form.getIntroduction().isEmpty());
        projectValidator.setNullOngoingAt(form.getProjectStartAt() == null || form.getProjectEndAt() == null);
        projectValidator.setNullSubmissionAt(form.getSubmissionStartAt() == null || form.getSubmissionEndAt() == null);
        projectValidator.setNullVotingAt(form.getVotingStartAt() == null || form.getVotingEndAt() == null);
        projectValidator.setNullThemeColor(form.getThemeColor() == null);


        projectValidator.updateStatus();

        return projectValidator;
    }
}
