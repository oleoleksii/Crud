package com.bymdev.service;

import com.bymdev.entity.Order;
import com.bymdev.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by oleksii on 17.12.16.
 */
@Service
@Transactional
public class OrderServiceImpl implements GenericService<Order> {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void create(Order order) {
        orderRepository.saveAndFlush(order);
    }

    @Override
    public Order read(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public List<Order> readAll() {
        return orderRepository.findAll();
    }

    @Override
    public void update(Order order) {
        orderRepository.saveAndFlush(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public boolean isExist(Order order) {
        return orderRepository.exists(order.getId());
    }
}