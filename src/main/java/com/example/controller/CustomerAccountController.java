package com.example.controller;

import com.example.entity.CustomerAccount;
import com.example.service.CustomerAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/customerAccount")
public class CustomerAccountController {

    private final CustomerAccountService customerAccountService;

    @GetMapping
    public List<CustomerAccount> getAll() {
        return customerAccountService.findAll();
    }

    @GetMapping(value = "/{id}")
    public CustomerAccount getById(@NotNull(message = "Id can not be empty") @PathVariable long id) {
        return customerAccountService.findById(id);
    }

    @PostMapping
    public CustomerAccount create(@Valid @RequestBody CustomerAccount customerAccount) {
        return customerAccountService.create(customerAccount);
    }

    @PutMapping
    public CustomerAccount update(@Valid @RequestBody CustomerAccount customerAccount) {
        return customerAccountService.update(customerAccount);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@NotNull(message = "Id can not be empty") @PathVariable long id) {
        customerAccountService.delete(id);
    }
}