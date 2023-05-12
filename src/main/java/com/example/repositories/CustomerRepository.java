package com.example.repositories;

import com.example.model.Customer;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface CustomerRepository  extends CrudRepository<Customer, String> {

    void deleteById(String id);
}