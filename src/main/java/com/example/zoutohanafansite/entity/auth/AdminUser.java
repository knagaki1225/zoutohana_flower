package com.example.zoutohanafansite.entity.auth;

import lombok.Data;

@Data
public class AdminUser {
    private long id;
    private String loginId;
    private String password;
    private String nickname;
}
