package com.example.zoutohanafansite.security;

import com.example.zoutohanafansite.entity.auth.User;
import com.example.zoutohanafansite.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByLoginId(username);

        if (user == null) {
            // throw exception
            return null;
        }

        return new CustomUserDetails(user);
    }
}
