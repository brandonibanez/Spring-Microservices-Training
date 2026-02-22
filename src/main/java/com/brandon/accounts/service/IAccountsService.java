package com.brandon.accounts.service;

import com.brandon.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     *
     * @param customerDto - CustomerDTO Object
     */
    void createAccount(CustomerDto customerDto);

}