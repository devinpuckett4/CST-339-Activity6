package com.gcu.topic2.data;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gcu.topic2.data.entity.OrderEntity;
import com.gcu.topic2.data.repository.OrdersRepository; // << correct package

@Service
public class OrdersDataService implements DataAccessInterface<OrderEntity> {

    private final OrdersRepository repo;

    public OrdersDataService(OrdersRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<OrderEntity> findAll() {
        return repo.findAll();
    }

    @Override
    public OrderEntity findById(String id) {
        return (id == null) ? null : repo.findById(id).orElse(null);
    }

    @Override
    public boolean create(OrderEntity t) {
        if (t == null) return false;
        repo.save(t);
        return true;
    }

    @Override
    public boolean update(OrderEntity t) {
        if (t == null || t.getId() == null) return false;
        repo.save(t);
        return true;
    }

    @Override
    public boolean delete(OrderEntity t) {
        if (t == null || t.getId() == null) return false;
        if (!repo.existsById(t.getId())) return false;
        repo.deleteById(t.getId());
        return true;
    }
}