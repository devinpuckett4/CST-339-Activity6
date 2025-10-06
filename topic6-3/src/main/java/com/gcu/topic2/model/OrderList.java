package com.gcu.topic2.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "orders")
public class OrderList {
    private List<OrderModel> orders = new ArrayList<>();

    public List<OrderModel> getOrders() { return orders; }
    public void setOrders(List<OrderModel> orders) { this.orders = orders; }
}