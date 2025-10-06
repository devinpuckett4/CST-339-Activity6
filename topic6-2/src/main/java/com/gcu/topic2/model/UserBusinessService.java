package com.gcu.topic2.model;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessService implements UserDetailsService {

    private final UsersDataService service;
    private final PasswordEncoder encoder;

    public UserBusinessService(UsersDataService service, PasswordEncoder encoder) {
        this.service = service; this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity ue = service.findByUsername(username);
        if (ue == null) throw new UsernameNotFoundException("User not found");
        return User.withUsername(ue.getUsername())
                .password(ue.getPassword()) // Already BCrypt in DB
                .roles("USER")
                .build();
    }
}