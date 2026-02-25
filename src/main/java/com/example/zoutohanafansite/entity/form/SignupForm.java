package com.example.zoutohanafansite.entity.form;

import com.example.zoutohanafansite.entity.enums.UserGender;
import lombok.Data;

@Data
public class SignupForm {
    private String loginId;
    private String Nickname;
    private Integer icon;
    private String address;
    private Integer birthYear;
    private UserGender userGender;
    private String password;
    private String confirmPassword;
    private String selfIntroduction;
}
