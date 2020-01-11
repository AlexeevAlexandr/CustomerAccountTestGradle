package com.example;

import com.example.entity.CustomerAccount;
import com.example.repository.CustomerAccountRepository;
import com.example.service.CustomerAccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAccountServiceTest {

    @InjectMocks
    private CustomerAccountServiceImpl customerAccountServiceImpl;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Test
    public void create() {
        //given
        CustomerAccount customerAccount = new CustomerAccount("MockTest Name", "MockTestEmail@gmail.com");
        doReturn(customerAccount).when(customerAccountRepository).save(customerAccount);
        //when
        CustomerAccount actualCustomerAccount = customerAccountServiceImpl.create(customerAccount);
        //then
        assertThat(actualCustomerAccount).isEqualTo(customerAccount);
    }

    @Test
    public void findAll() {
        //given
        List<CustomerAccount> customerAccounts = Arrays.asList(
                new CustomerAccount("MockTest Name1", "MockTestEmail1@gmail.com"),
                new CustomerAccount("MockTest Name2", "MockTestEmail2@gmail.com")
        );
        doReturn(customerAccounts).when(customerAccountRepository).findAll();
        //when
        List<CustomerAccount> actualCustomerAccounts = customerAccountServiceImpl.findAll();
        //then
        assertThat(actualCustomerAccounts).isEqualTo(customerAccounts);
    }

    @Test
    public void findById() {
        //given
        CustomerAccount customerAccount = new CustomerAccount("MockTest Name", "MockTestEmail@gmail.com");
        doReturn(customerAccount).when(customerAccountRepository).findById(1L);
        doReturn(true).when(customerAccountRepository).existsById(1L);
        //when
        CustomerAccount actualCustomerAccount = customerAccountServiceImpl.findById(1L);
        //then
        assertThat(actualCustomerAccount).isEqualTo(customerAccount);
    }

    @Test
    public void update() {
        //given
        CustomerAccount customerAccount = new CustomerAccount("MockTest Name", "MockTestEmail@gmail.com");
        customerAccount.setId(1L);
        doReturn(customerAccount).when(customerAccountRepository).save(customerAccount);
        doReturn(true).when(customerAccountRepository).existsById(1L);
        //when
        CustomerAccount actualCustomerAccount = customerAccountServiceImpl.update(customerAccount);
        //then
        assertThat(actualCustomerAccount).isEqualTo(customerAccount);
    }

    @Test
    public void delete() {
        //given
        doReturn(true).when(customerAccountRepository).existsById(1L);
        //when
        customerAccountServiceImpl.delete(1L);
        //then
        verify(customerAccountRepository, times(1)).deleteById(1L);
    }

    @Test
    public void exists() {
        //given
        doReturn(true).when(customerAccountRepository).existsById(1L);
        //when
        boolean flag = customerAccountServiceImpl.exists(1L);
        //then
        assertThat(flag).isEqualTo(true);
    }
}
