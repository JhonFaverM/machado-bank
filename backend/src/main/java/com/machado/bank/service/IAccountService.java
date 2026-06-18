package com.machado.bank.service;

import com.machado.bank.model.Account;

import java.util.List;

public interface IAccountService {

    Account createAccount(Long clientId);


    Account findByAccountNumber(String accountNumber, Long clientId);

    List<Account> findByClient(Long clientId);

    Account findById(Long id);

    // OPERACIONES DE SEGURIDAD
    Account blockAccount(String accountNumber, Long clientId);

    Account closeAccount(String accountNumber, Long clientId);

    Account activateAccount(String accountNumber, Long clientId);
}