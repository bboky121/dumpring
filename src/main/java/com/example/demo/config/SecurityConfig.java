package com.example.demo.config;

import com.example.demo.security.JwtTokenFilter;
import com.example.demo.service.CustomOAuth2UserService;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/oauth2/**", "/login/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll() // 사용자 생성은 모든 사용자에게 허용
                .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN") // 인증된 사용자만 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN") // 인증된 사용자만 접근 가능
                .anyRequest().authenticated() // 그 외 요청은 모두 인증된 사용자만 접근 가능
            )
            .oauth2Login(withDefaults -> {
                withDefaults.loginPage("/login");
                withDefaults.defaultSuccessUrl("/users", true);
                withDefaults.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService));
            })
            .formLogin(form -> form
                    .loginPage("/login").defaultSuccessUrl("/users", true).permitAll() // 사용자 정의 로그인 페이지 설정
            )
            .logout(LogoutConfigurer::permitAll); // 로그아웃 설정

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationConfiguration authenticationConfiguration() throws Exception {
        return new AuthenticationConfiguration();
    }
}