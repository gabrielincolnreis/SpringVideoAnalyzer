package com.example.services;

import com.example.model.Customer;
import com.example.repositories.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
       return customerRepository.save(customer);
    }

    @Override
    public Iterable<Customer> getAll(){
        return customerRepository.findAll();

    }

    @Override
    public ResponseEntity<?> delete(String id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if(optional.isPresent()){
            customerRepository.deleteById(id);
            return ResponseEntity.ok("Removido com sucesso");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public ResponseEntity<Object> update(Customer customer, String id) {
        Optional<Customer> customerOptional =  customerRepository.findById(id);
        if(customerOptional.isPresent()){
            customerOptional.get().setName(customer.name);
            customerOptional.get().setEmail(customer.email);
            customerRepository.save(customerOptional.get());
            return ResponseEntity.ok(customerOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
