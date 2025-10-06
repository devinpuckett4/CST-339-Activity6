package com.gcu.topic2.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gcu.topic2.data.DataAccessInterface;
import com.gcu.topic2.data.entity.OrderEntity;
import com.gcu.topic2.model.OrderModel;

@Service
public class OrdersBusinessService implements OrdersBusinessInterface {

    private final DataAccessInterface<OrderEntity> service;

    public OrdersBusinessService(DataAccessInterface<OrderEntity> service) {
        this.service = service;
    }

    @Override
    public void test() {
        System.out.println("Hello from the OrdersBusinessService.test()");
    }

    @Override
    public List<OrderModel> getOrders() {
        return service.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public OrderModel getOrderById(String id) {
        OrderEntity e = service.findById(id);
        return (e == null) ? null : toModel(e);
    }

    private OrderModel toModel(OrderEntity e) {
        return new OrderModel(
            e.getId(),
            e.getOrderNo(),
            e.getProductName(),
            e.getPrice(),
            e.getQuantity()
        );
    }
}