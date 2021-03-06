package com.example.service;

import com.example.entity.CustomerAccount;
import com.example.exception.CustomerAccountException;
import com.example.repository.CustomerAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CustomerAccountServiceImpl implements CustomerAccountService{

    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public CustomerAccount create(CustomerAccount customerAccount) {
        try {
            log.info("attempt to create CustomerAccount");
            customerAccount = customerAccountRepository.save(customerAccount);
            log.info("attempt to create CustomerAccount - success");
            return customerAccount;
        } catch (Exception e) {
            log.debug("attempt to create CustomerAccount - failed\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CustomerAccount> findAll() {
        try {
            log.info("attempt to get CustomerAccount list");
            List<CustomerAccount> customerAccounts = customerAccountRepository.findAll();
            log.info("attempt to get CustomerAccount list - success");
            return customerAccounts;
        } catch (Exception e) {
            log.debug("attempt to get CustomerAccount list - failed\n" + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public CustomerAccount findById(long id) {
        if (!exists(id)) {
            throw new CustomerAccountException("CustomerAccount not found");
        }
        try {
            log.info("attempt to get CustomerAccount by id");
            CustomerAccount customerAccount = customerAccountRepository.findById(id);
            log.info("attempt to get CustomerAccount by id - success");
            return customerAccount;
        } catch (Exception e) {
            log.debug("attempt to get CustomerAccount by id - failed\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public CustomerAccount update(CustomerAccount customerAccount) {
        if (customerAccount.getId() == null) {
            throw new CustomerAccountException("Id can not be null");
        }
        if (!exists(customerAccount.getId())) {
            throw new CustomerAccountException("Can not update, id not exist");
        }
        try {
            log.info("attempt to update CustomerAccount");
            customerAccount = customerAccountRepository.save(customerAccount);
            log.info("attempt to update CustomerAccount - success");
            return customerAccount;
        } catch (Exception e) {
            log.debug("attempt to update CustomerAccount - failed\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(long id) {
        if (!exists(id)) {
            throw new CustomerAccountException("CustomerAccount not found");
        }
        try {
            log.info("attempt to delete CustomerAccount");
            customerAccountRepository.deleteById(id);
            log.info("attempt to delete CustomerAccount - success");
        } catch (Exception e) {
            log.debug("attempt to delete CustomerAccount - failed\n" + e.getMessage());
        }
    }

    @Override
    public boolean exists(long id) {
        try {
            log.info("check if exists CustomerAccount");
            boolean flag = customerAccountRepository.existsById(id);
            log.info("check if exists CustomerAccount - success");
            return flag;
        } catch (Exception e) {
            log.debug("check if exists CustomerAccount - failed\n" + e.getMessage());
            return false;
        }
    }
}
