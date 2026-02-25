package com.example.zoutohanafansite.security;

import com.example.zoutohanafansite.entity.auth.AdminUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomAdminUserDetails implements UserDetails {
    private final AdminUser adminUser;

    public CustomAdminUserDetails(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }

    public long getUserId(){return adminUser.getId();}

    @Override
    public String getUsername() {
        return adminUser.getLoginId();
    }

    public String getAdminUserNickname(){return adminUser.getNickname();}

    @Override
    public String getPassword(){
        return this.adminUser.getPassword();
    }

    public boolean isMember() {
        return false;
    }

    public boolean isAdmin() {
        return true;
    }
}
