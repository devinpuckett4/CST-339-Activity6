package com.gcu.topic64.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class MyService {
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal OAuth2User user) {
        String name = (user != null && user.getAttribute("login") != null)
                ? user.getAttribute("login").toString()
                : "anonymous";
        return "hello " + name;
    }
}
