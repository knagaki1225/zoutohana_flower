package com.example.zoutohanafansite.entity.form;

import com.example.zoutohanafansite.entity.enums.UserGender;
import lombok.Data;

@Data
public class AccountEditForm {
    private String nickname;
    private int icon;
    private String address;
    private Integer birthYear;
    private UserGender userGender;
    private String selfIntroduction;
}
