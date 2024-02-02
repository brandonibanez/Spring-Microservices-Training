package com.brandoncode.accounts.repository;

import com.brandoncode.accounts.entity.Accounts;
import com.brandoncode.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

}
