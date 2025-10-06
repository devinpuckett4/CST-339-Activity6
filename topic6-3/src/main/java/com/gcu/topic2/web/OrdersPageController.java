package com.gcu.topic2.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gcu.topic2.business.OrdersBusinessInterface;

@Controller
public class OrdersPageController {

    private final OrdersBusinessInterface service;

    public OrdersPageController(OrdersBusinessInterface service) {
        this.service = service;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", service.getOrders());
        return "orders";
    }
}