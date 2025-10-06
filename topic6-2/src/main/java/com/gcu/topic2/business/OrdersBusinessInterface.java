package com.gcu.topic2.business;

import java.util.List;

import com.gcu.topic2.model.OrderModel;

public interface OrdersBusinessInterface {
    void test();
    List<OrderModel> getOrders();
    OrderModel getOrderById(String id); 
}