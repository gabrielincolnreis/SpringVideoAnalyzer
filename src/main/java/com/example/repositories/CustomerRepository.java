package com.example.repositories;

import com.example.model.Customer;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface CustomerRepository  extends PagingAndSortingRepository<Customer, Long> {

    Customer save(Customer customer);
}