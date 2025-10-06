package com.gcu.topic2.config;

import com.gcu.topic2.model.UserBusinessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserBusinessService userDetails;

    public SecurityConfig(UserBusinessService userDetails) {
        this.userDetails = userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetails);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authProvider());
        http.csrf(cs -> cs.disable());
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/getjson", "/getxml").permitAll()
            .anyRequest().authenticated()
        );
        http.formLogin(fl -> fl.loginPage("/login").permitAll().defaultSuccessUrl("/orders/display", true));
        http.logout(lo -> lo.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
}