package com.gcu.topic2.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class HashOnceRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("BCrypt for 'test': " + new BCryptPasswordEncoder().encode("test"));
    }
}