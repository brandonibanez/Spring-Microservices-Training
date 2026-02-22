package com.brandon.accounts.service.impl;

import org.springframework.stereotype.Service;

import com.brandon.accounts.dto.CustomerDto;
import com.brandon.accounts.entity.Customer;
import com.brandon.accounts.repository.AccountsRepository;
import com.brandon.accounts.repository.CustomerRepository;
import com.brandon.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {

    }

}
