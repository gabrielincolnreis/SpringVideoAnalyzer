package com.example.services;

import com.example.model.Order;
import com.example.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



public class OrderServiceImpl {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(final Order order) {
        orderRepository.save(order);
    }

}
