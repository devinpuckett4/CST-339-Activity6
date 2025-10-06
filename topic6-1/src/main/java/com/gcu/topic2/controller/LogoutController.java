package com.gcu.topic2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        // No Spring Security session to clear in part 1; just navigate back to login.
        return "redirect:/login";
    }
}