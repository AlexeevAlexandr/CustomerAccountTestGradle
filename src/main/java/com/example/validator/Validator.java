package com.example.validator;

import com.example.entity.CustomerAccount;

public interface Validator {

    void isNull(Long id);

    long parseLong(String id);

    void isEmptyCustomerAccount(CustomerAccount customerAccount);
}
