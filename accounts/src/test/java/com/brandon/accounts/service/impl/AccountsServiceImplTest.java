package com.brandon.accounts.service.impl;

import com.brandon.accounts.dto.AccountsDto;
import com.brandon.accounts.dto.AccountsMsgDto;
import com.brandon.accounts.dto.CustomerDto;
import com.brandon.accounts.entity.Accounts;
import com.brandon.accounts.entity.Customer;
import com.brandon.accounts.exception.CustomerAlreadyExistsException;
import com.brandon.accounts.repository.AccountsRepository;
import com.brandon.accounts.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsServiceImplTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private AccountsServiceImpl accountsService;

    private CustomerDto customerDto;

    private Customer customer;

    private Accounts account;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");
        customerDto.setMobileNumber("9876543210");

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setMobileNumber("9876543210");

        account = new Accounts();
        account.setAccountNumber(1234567890L);
        account.setCustomerId(1L);
        account.setAccountType("SAVINGS");
        account.setBranchAddress("123 Main St");
    }

    @Nested
    @DisplayName("Create Account Tests")
    class CreateAccountTests {
        @Test
        void testCreateAccountSuccessfully() {
            // Given
            when(customerRepository.findByMobileNumber("9876543210"))
                    .thenReturn(Optional.empty());

            when(customerRepository.save(any(Customer.class)))
                    .thenReturn(customer);

            when(accountsRepository.save(any(Accounts.class)))
                    .thenReturn(account);

            when(streamBridge.send(eq("sendCommunication-out-0"), any(AccountsMsgDto.class)))
                    .thenReturn(true);

            // When
            accountsService.createAccount(customerDto);

            // Then
            verify(customerRepository).findByMobileNumber("9876543210");
            verify(customerRepository).save(any(Customer.class));
            verify(accountsRepository).save(any(Accounts.class));

            ArgumentCaptor<AccountsMsgDto> captor = ArgumentCaptor.forClass(AccountsMsgDto.class);
            verify(streamBridge).send(eq("sendCommunication-out-0"), captor.capture());

            AccountsMsgDto sentMessage = captor.getValue();
            assertEquals("John Doe", sentMessage.name());
            assertEquals("john.doe@example.com", sentMessage.email());
            assertEquals("9876543210", sentMessage.mobileNumber());
            assertEquals(1234567890L, sentMessage.accountNumber());
        }

        @Test
        void testCreateAccountThrowsExceptionWhenCustomerAlreadyExists() {
            // Given
            Customer existingCustomer = new Customer();
            existingCustomer.setCustomerId(1L);
            existingCustomer.setMobileNumber("9876543210");

            when(customerRepository.findByMobileNumber("9876543210"))
                    .thenReturn(Optional.of(existingCustomer));

            // When & Then
            CustomerAlreadyExistsException exception = assertThrows(
                    CustomerAlreadyExistsException.class,
                    () -> accountsService.createAccount(customerDto)
            );

            assertTrue(exception.getMessage().contains("Customer already registered"));
            assertTrue(exception.getMessage().contains("9876543210"));

            verify(customerRepository, never()).save(any());
            verify(accountsRepository, never()).save(any());
            verify(streamBridge, never()).send(anyString(), any());
        }
    }

    @Test
    void testFetchAccountSuccessfully() {
        //Given
        when(customerRepository.findByMobileNumber("9876543210"))
        .thenReturn(Optional.of(customer));

        when(accountsRepository.findByCustomerId(1L))
        .thenReturn(Optional.of(account));  
        //When

        CustomerDto result = accountsService.fetchAccount("9876543210");

        //Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("9876543210", result.getMobileNumber());

        AccountsDto accountsDto = result.getAccountsDto();
        assertNotNull(accountsDto);
        assertEquals(1234567890L, accountsDto.getAccountNumber());
        assertEquals("SAVINGS", accountsDto.getAccountType());
        assertEquals("123 Main St", accountsDto.getBranchAddress());

        verify(customerRepository).findByMobileNumber("9876543210");
        verify(accountsRepository).findByCustomerId(1L);
    }
}
