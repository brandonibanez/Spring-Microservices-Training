package com.brandon.accounts.service.impl;

import com.brandon.accounts.dto.AccountsDto;
import com.brandon.accounts.dto.CardsDto;
import com.brandon.accounts.dto.CustomerDetailsDto;
import com.brandon.accounts.dto.LoansDto;
import com.brandon.accounts.entity.Accounts;
import com.brandon.accounts.entity.Customer;
import com.brandon.accounts.exception.ResourceNotFoundException;
import com.brandon.accounts.mapper.AccountsMapper;
import com.brandon.accounts.mapper.CustomerMapper;
import com.brandon.accounts.repository.AccountsRepository;
import com.brandon.accounts.repository.CustomerRepository;
import com.brandon.accounts.service.ICustomersService;
import com.brandon.accounts.service.client.CardsFeignClient;
import com.brandon.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}