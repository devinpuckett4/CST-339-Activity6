package com.gcu.topic2.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.gcu.topic2.data.entity.OrderEntity;

public interface OrdersRepository extends MongoRepository<OrderEntity, String> {
    OrderEntity getOrderById(String id); 
}