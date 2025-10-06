package com.gcu.topic2.business;

import com.gcu.topic2.data.UsersDataService;
import com.gcu.topic2.data.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class UserBusinessService implements UserDetailsService {

    private final UsersDataService users;

    public UserBusinessService(UsersDataService users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = users.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("User not found: " + username);
        return User.withUsername(u.getUsername())
                .password(u.getPassword())   // BCrypt stored in DB
                .roles("USER")
                .build();
    }
}