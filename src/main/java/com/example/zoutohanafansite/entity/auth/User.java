package com.example.zoutohanafansite.entity.auth;

import com.example.zoutohanafansite.entity.enums.UserGender;
import com.example.zoutohanafansite.entity.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Year;

@Data
public class User {
    private long id;
    private String loginId;
    private String nickname;
    private String password;
    private String selfIntroduction;
    private int icon;
    private String address;
    private int birthYear;
    private UserGender gender;
    private String securityKey;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private long reviewCount;

    public int getAge() {
        return Year.now().getValue() - birthYear;
    }
}
