package com.gcu.topic2.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import com.gcu.topic2.data.OrdersDataService;
import com.gcu.topic2.data.entity.OrderEntity;
import com.gcu.topic2.model.OrderModel;
import java.util.*;

@Controller
public class OrdersPageController {
    @Autowired
    private OrdersDataService service;
    @GetMapping("/orders")
    public String orders(Model model) {
        List<OrderModel> out = new ArrayList<>();
        try {
            for (OrderEntity e : service.findAll()) {
                out.add(new OrderModel(e.getId(), e.getOrderNo(), e.getProductName(), e.getPrice(), e.getQuantity()));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        model.addAttribute("orders", out);
        return "orders";
    }
}