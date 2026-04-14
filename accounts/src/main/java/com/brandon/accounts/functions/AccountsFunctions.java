package com.brandon.accounts.functions;

import com.brandon.accounts.service.IAccountsService;

import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

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
            input.foreach((key, accountNumber) -> {
                log.info("Kafka Streams: Processing account + 1: " + (Integer.parseInt(accountNumber) + 1));
            });
        };
    }

}
