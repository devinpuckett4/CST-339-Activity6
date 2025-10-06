package com.gcu.topic2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@Profile("printpass")
public class PasswordPrinter implements CommandLineRunner {
    @Override
    public void run(String... args) {
        String plain = System.getProperty("printpass");
        if (plain != null) {
            System.out.println("BCrypt: " + new BCryptPasswordEncoder().encode(plain));
        }
    }
}