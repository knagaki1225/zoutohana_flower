package com.example.zoutohanafansite.security;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.entity.enums.UserGender;
import com.example.zoutohanafansite.entity.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public long getUserId(){return user.getId();}

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    public String getUserNickname(){return user.getNickname();}

    @Override
    public String getPassword(){
        return this.user.getPassword();
    }

    public String getUserSelfIntroduction(){return user.getSelfIntroduction();}

    public int getIcon(){return user.getIcon();}

    public String getAddress(){return user.getAddress();}

    public int getBirthYear(){return user.getBirthYear();}

    public UserGender getGender(){return user.getGender();}

    public String getSecurityKey(){return user.getSecurityKey();}

    public UserStatus getUserStatus(){return user.getStatus();}

    public boolean isMember() {
        return true;
    }

    public boolean isAdmin() {
        return false;
    }

}
