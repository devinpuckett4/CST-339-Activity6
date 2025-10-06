package com.gcu.topic2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.topic2.business.OrdersBusinessInterface;
import com.gcu.topic2.business.SecurityBusinessService;
import com.gcu.topic2.model.LoginModel;
import com.gcu.topic2.model.OrderModel;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private OrdersBusinessInterface service;

    @Autowired
    private SecurityBusinessService security;

    // GET /login/  -> show login form
    @GetMapping({"", "/"})
    public String display(Model model) {
        model.addAttribute("title", "Login Form");
        model.addAttribute("loginModel", new LoginModel());
        return "login";
    }

    // POST /login/doLogin -> validate, then show orders
    @PostMapping("/doLogin")
    public String doLogin(@Valid @ModelAttribute("loginModel") LoginModel loginModel,
                          BindingResult bindingResult,
                          Model model) {

        // If validation fails, stay on the form and show errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Login Form");
            return "login";
        }
        service.test();

        boolean ok = security.authenticate(loginModel.getUsername(), loginModel.getPassword());
        if (!ok) {
            model.addAttribute("title", "Login Form");
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        // Get orders from the business service 
        List<OrderModel> orders = service.getOrders();

        model.addAttribute("title", "My Orders");
        model.addAttribute("orders", orders);
        return "orders"; // templates/orders.html
    }
}
