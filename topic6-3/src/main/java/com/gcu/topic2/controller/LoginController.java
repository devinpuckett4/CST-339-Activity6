// src/main/java/com/gcu/topic2/controller/LoginController.java
package com.gcu.topic2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() { return "login"; }
}