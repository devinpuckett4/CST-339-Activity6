package com.gcu.topic2.business;

import java.util.List;

import com.gcu.topic2.model.OrderModel;

public interface OrdersBusinessInterface {
    List<OrderModel> getOrders();
}