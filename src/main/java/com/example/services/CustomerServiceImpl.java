package com.example.services;

import com.example.model.Customer;
import com.example.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;

    @Autowired
    public void setClientRepository(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
       return customerRepository.save(customer);
    }

//    @Override
//    public List<Customer> fetchList() {
//        return (List<Customer>) customerRepository.findAll();
//    }
//
//    @Override
//    public Customer update(Customer customer, Long customerID) {
//        boolean customerExist = customerRepository.findById(customerID).isPresent();
//        Customer customerFound = new Customer();
//        if(customerExist){
//            customerFound = customerRepository.findById(customerID).get();
//        }
//        return customerRepository.save(customerFound);
//    }
//
//    @Override
//    public void deleteById(Long customerID) {
//        customerRepository.deleteById(customerID);
//    }
}
