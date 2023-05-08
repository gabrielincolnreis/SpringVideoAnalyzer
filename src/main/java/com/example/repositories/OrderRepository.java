package com.example.repositories;

import com.example.model.Order;
import com.example.model.OrderID;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@EnableScan
//@Repository
public interface OrderRepository  extends CrudRepository<Order, OrderID> {

}