package com.example.controller;

import com.example.model.Customer;
import com.example.services.CustomerService;
import com.example.video.util.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    S3Service s3Client;

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @GetMapping()
    public Iterable<Customer> getAll() {
        return customerService.getAll();
    }

    @PostMapping()
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Customer customer) {
        return customerService.update(customer,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        return customerService.delete(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImagensS3(@RequestParam("faces") MultipartFile faces) throws IOException {
           return s3Client.addObject(faces);
    }
}
