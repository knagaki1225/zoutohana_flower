package com.example.zoutohanafansite.entity.form;

import lombok.Data;

@Data
public class PasswordResetForm {
    private String loginId;
    private String securityKey;
}
