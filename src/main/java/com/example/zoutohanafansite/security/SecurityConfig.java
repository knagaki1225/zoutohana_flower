package com.example.zoutohanafansite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAdminUserDetailsService adminDetailsService;

    public SecurityConfig(CustomLoginSuccessHandler customLoginSuccessHandler, CustomUserDetailsService customUserDetailsService, CustomAdminUserDetailsService adminDetailsService) {
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.adminDetailsService = adminDetailsService;
    }

    // --- 管理者向け設定 ---
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**") // /admin以下のURLに適用
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")       // 管理者専用ログイン画面
                        .defaultSuccessUrl("/admin/dash")
                )
                .userDetailsService(adminDetailsService); // ここで管理者用DBロジックを指定

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/leave/complete").permitAll()
                        // 2. 認証だけでなく「ロール」もチェックするとより安全
                        .requestMatchers("/review/**", "/mypage/**").hasRole("USER")
                        .requestMatchers("/auth/**").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // API関連はCSRFチェックをスキップ
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .loginPage("/login")
                        .failureHandler(new CustomLoginFailureHandler())
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
                )
                // 3. このFilterChainで使用するUserDetailsServiceを明示的に指定
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
