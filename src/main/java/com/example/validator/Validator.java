package com.example.validator;

import com.example.entity.CustomerAccount;
import com.example.exception.CustomerAccountException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    public void isNull(Long id) {
        if (id == null) {
            throw new CustomerAccountException("Id can not be null");
        }
    }

    public long parseLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new CustomerAccountException("Id must be a digit");
        }
    }

    public void isEmptyCustomerAccount(CustomerAccount customerAccount){
        if (customerAccount.getName() == null){
            throw new CustomerAccountException("Name can not be null");
        }

        if (customerAccount.getName().isEmpty()){
            throw new CustomerAccountException("Name can not be empty");
        }

        if (!EmailValidator.getInstance().isValid(customerAccount.getEmail())){
            throw new CustomerAccountException("Email address not valid");
        }

    }
}
