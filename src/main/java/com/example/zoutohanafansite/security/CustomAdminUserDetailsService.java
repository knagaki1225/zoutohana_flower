package com.example.zoutohanafansite.security;

import com.example.zoutohanafansite.entity.auth.AdminUser;
import com.example.zoutohanafansite.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAdminUserDetailsService implements UserDetailsService {
    private final AdminUserRepository adminUserRepository;

    public CustomAdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.getAdminUserByLoginId(username);
        if(adminUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomAdminUserDetails(adminUserRepository.getAdminUserByLoginId(username));
    }
}
