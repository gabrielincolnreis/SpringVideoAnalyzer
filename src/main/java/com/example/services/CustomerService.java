package com.example.services;

import com.example.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CustomerService {

    Customer save(Customer customer);
}
