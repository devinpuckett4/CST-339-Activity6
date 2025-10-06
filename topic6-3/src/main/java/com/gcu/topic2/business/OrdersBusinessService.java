package com.gcu.topic2.business;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gcu.topic2.data.OrdersDataService;
import com.gcu.topic2.data.entity.OrderEntity;
import com.gcu.topic2.model.OrderModel;

@Service
public class OrdersBusinessService implements OrdersBusinessInterface {

    private final OrdersDataService data;

    public OrdersBusinessService(OrdersDataService data) {
        this.data = data;
    }

    @Override
    public List<OrderModel> getOrders() {
        return data.findAll()
                   .stream()
                   .map(this::toModel)
                   .collect(Collectors.toList());
    }

    private OrderModel toModel(OrderEntity e) {
        if (e == null) return null;

        // e.getPrice() is a primitive double, so it can never be null.
        BigDecimal price = BigDecimal.valueOf(e.getPrice());

        return new OrderModel(
                e.getId(),
                e.getOrderNumber(),
                e.getProduct(),
                price,
                e.getQuantity()
        );
    }
}