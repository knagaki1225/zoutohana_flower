package com.example.zoutohanafansite.entity.user;

import com.example.zoutohanafansite.entity.enums.UserGender;
import com.example.zoutohanafansite.entity.enums.UserStatus;
import lombok.Data;

import java.time.Year;

@Data
public class AdminUserList {
    private long id;
    private String loginId;
    private String nickname;
    private UserStatus userStatus;
    private UserGender userGender;
    private int birthYear;
    private String address;
    private int reviewCount;
}
