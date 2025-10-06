package com.gcu.topic2.business;

import org.springframework.stereotype.Service;

@Service
public class SecurityBusinessService {
    public boolean authenticate(String username, String password) {
        // TEMP: accept a simple known pair until real auth is in
        return "test".equals(username) && "test".equals(password);
    }
}