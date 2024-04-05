package com.brandoncode.accounts.service.impl;

import com.brandoncode.accounts.dto.AccountsDto;
import com.brandoncode.accounts.dto.CardsDto;
import com.brandoncode.accounts.dto.CustomerDetailsDto;
import com.brandoncode.accounts.dto.LoansDto;
import com.brandoncode.accounts.entity.Accounts;
import com.brandoncode.accounts.entity.Customer;
import com.brandoncode.accounts.exception.ResourceNotFoundException;
import com.brandoncode.accounts.mapper.AccountsMapper;
import com.brandoncode.accounts.mapper.CustomerMapper;
import com.brandoncode.accounts.repository.AccountsRepository;
import com.brandoncode.accounts.repository.CustomerRepository;
import com.brandoncode.accounts.service.ICustomersService;
import com.brandoncode.accounts.service.client.CardsFeignClient;
import com.brandoncode.accounts.service.client.LoansFeignClient;
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
