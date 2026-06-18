package com.machado.bank.service;

import com.machado.bank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {
    Transaction deposit(String accountNumber, BigDecimal amount, Long clientId);

    Transaction withdraw(String accountNumber, BigDecimal amount, Long clientId);

    String transfer(String fromAccountNumber,
                    String toAccountNumber,
                    BigDecimal amount,
                    Long clientId);



    List<Transaction> findByAccount(String accountNumber, Long clientId);

    List<Transaction> findByReference(String referenceCode, Long clientId);
}
