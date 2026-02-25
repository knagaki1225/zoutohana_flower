package com.example.zoutohanafansite.entity.form;

import lombok.Data;

@Data
public class NewPasswordForm {
    private String loginId;
    private String password;
    private String confirmPassword;
}
