package com.example.services;

import com.example.model.Customer;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CustomerService {

    Customer save(Customer customer);

    Iterable<Customer> getAll();

    ResponseEntity<? extends Object> delete(String id);

    Optional<Customer> findById(String id);

    ResponseEntity<Object> update(Customer customer, String id);
}
