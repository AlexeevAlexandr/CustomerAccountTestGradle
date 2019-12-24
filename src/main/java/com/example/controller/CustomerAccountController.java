package com.example.controller;

import com.example.entity.CustomerAccount;
import com.example.service.CustomerAccountService;
import com.example.validator.Validator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customerAccount")
public class CustomerAccountController {

    private final CustomerAccountService customerAccountService;
    private final Validator validator;

    public CustomerAccountController(CustomerAccountService customerAccountService, Validator validator) {
        this.customerAccountService = customerAccountService;
        this.validator = validator;
    }

    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<CustomerAccount> getAll() {
        return customerAccountService.findAll();
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public CustomerAccount getById(@PathVariable String id) {
        return customerAccountService.findById(validator.parseLong(id));
    }

    @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public CustomerAccount create(@RequestBody CustomerAccount customerAccount) {
        validator.isEmptyCustomerAccount(customerAccount);
        return customerAccountService.create(customerAccount);
    }

    @PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public CustomerAccount update(@RequestBody CustomerAccount customerAccount) {
        validator.isNull(customerAccount.getId());
        validator.isEmptyCustomerAccount(customerAccount);
        return customerAccountService.update(customerAccount);
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public void delete(@PathVariable String id) {
        customerAccountService.delete(validator.parseLong(id));
    }
}