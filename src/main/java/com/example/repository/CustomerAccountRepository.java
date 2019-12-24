package com.example.repository;

import com.example.entity.CustomerAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAccountRepository extends CrudRepository<CustomerAccount, Long> {

    @Override
    List<CustomerAccount> findAll();

    CustomerAccount findById(long id);
}
