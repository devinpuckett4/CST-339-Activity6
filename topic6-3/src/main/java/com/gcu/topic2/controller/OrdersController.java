package com.gcu.topic2.controller;


import com.gcu.topic2.business.OrdersBusinessService;
import com.gcu.topic2.model.OrderModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersBusinessService service;

    public OrdersController(OrdersBusinessService service) {
        this.service = service;
    }

    @GetMapping("/display")
    public String displayOrders(Model model) {
        List<OrderModel> orders = service.getOrders();
        model.addAttribute("title", "My Orders");
        model.addAttribute("orders", orders);
        return "orders";
    }
}
