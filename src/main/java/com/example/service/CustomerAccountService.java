package com.example.service;

import com.example.entity.CustomerAccount;

import java.util.List;

public interface CustomerAccountService {

    CustomerAccount create(CustomerAccount customerAccount);

    List<CustomerAccount> findAll();

    CustomerAccount findById(long id);

    CustomerAccount update(CustomerAccount customerAccount);

    void delete(long id);

    boolean exists(long id);
}
