package com.brandon.accounts.functions;

import com.brandon.accounts.service.IAccountsService;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {

    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Bean
    public Consumer<Long> updateCommunication(IAccountsService accountsService) {
        return accountNumber -> {
            log.info("Updating Communication status for the account number : " +
                    accountNumber.toString());
            accountsService.updateCommunicationStatus(accountNumber);
        };
    }

    @Bean
    public Consumer<KStream<String, String>> analytics(IAccountsService accountsService) {
        return input -> {
            input
                    // 1. Group by the value (accountNumber)
                    .groupBy((key, accountNumber) -> accountNumber)
                    // 2. Count occurrences (this creates a KTable, which is stateful)
                    .count(Materialized.as("account-counts-store"))
                    // 3. Convert back to stream to log the results
                    .toStream()
                    .foreach((accountNumber, count) -> {
                        log.info("Account " + accountNumber + " has been seen " + count + " times.");
                    });
        };
    }

}
