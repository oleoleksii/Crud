package com.bymdev.service;

import com.bymdev.entity.OrderItem;
import com.bymdev.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by oleksii on 17.12.16.
 */
@Service
@Transactional
public class OrderItemServiceImpl implements GenericService<OrderItem> {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void create(OrderItem orderItem) {
        orderItemRepository.saveAndFlush(orderItem);
    }

    @Override
    public OrderItem read(Long id) {
        return orderItemRepository.findOne(id);
    }

    @Override
    public List<OrderItem> readAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemRepository.saveAndFlush(orderItem);
    }

    @Override
    public void delete(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }

    @Override
    public boolean isExist(OrderItem orderItem) {
        return orderItemRepository.exists(orderItem.getId());
    }
}