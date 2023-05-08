package com.example.controller;

import com.example.model.Customer;
import com.example.repositories.CustomerRepository;
import com.example.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping("/save")
    public Customer saveCustomer(
            @Valid @RequestBody Customer customer)
    {
        return customerService.save(customer);
    }
}
