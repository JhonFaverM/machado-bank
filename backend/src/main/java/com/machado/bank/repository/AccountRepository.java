package com.machado.bank.repository;

import com.machado.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByClientId(Long clientId);

    boolean existsByClientId(Long clientId);
}
