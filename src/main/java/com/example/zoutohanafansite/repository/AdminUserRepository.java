package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.auth.AdminUser;
import com.example.zoutohanafansite.mapper.AdminUserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AdminUserRepository {
    private final AdminUserMapper adminUSerMapper;

    public AdminUserRepository(AdminUserMapper adminUSerMapper) {
        this.adminUSerMapper = adminUSerMapper;
    }

    public AdminUser getAdminUserByLoginId(String loginId){
        return adminUSerMapper.getAdminUserByLoginId(loginId);
    }
}
