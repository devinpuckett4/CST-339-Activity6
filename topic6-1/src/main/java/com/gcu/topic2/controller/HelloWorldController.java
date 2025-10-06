package com.gcu.topic2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hello")
public class HelloWorldController {

    // GET /hello
    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("message", "Hello from /hello");
        model.addAttribute("message2", "Use the links below to try test2, test3, and test4.");
        return "hello";
    }

    // GET /hello/test1 -> plain text
    @GetMapping("/test1")
    @ResponseBody
    public String printHelloText() {
        return "Hello World!";
    }

    // GET /hello/test2 -> hello view with one message
    @GetMapping("/test2")
    public String printHello(Model model) {
        model.addAttribute("message", "Hello Spring MVC Framework!");
        return "hello";
    }

    // GET /hello/test3 -> hello view with two messages 
    @GetMapping("/test3")
    public ModelAndView printHello1() {
        ModelAndView mav = new ModelAndView("hello");
        mav.addObject("message", "Hello World from ModelAndView!");
        mav.addObject("message2", "Another Hello World from ModelAndView!");
        return mav;
    }

    @GetMapping("/test4")
    public String printHelloParam(@RequestParam("message") String message, Model model) {
        model.addAttribute("message", message);
        return "hello";
    }
}