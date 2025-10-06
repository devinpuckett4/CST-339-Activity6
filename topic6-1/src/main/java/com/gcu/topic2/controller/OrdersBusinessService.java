package com.gcu.topic2.controller;

import com.gcu.topic2.model.OrderModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrdersBusinessService {
    public List<OrderModel> getOrders() {
        return Arrays.asList(
                new OrderModel("A100", "Laptop", 1, 999.99),
                new OrderModel("A101", "Headphones", 2, 59.99),
                new OrderModel("A102", "Mouse", 3, 24.99)
        );
    }
}