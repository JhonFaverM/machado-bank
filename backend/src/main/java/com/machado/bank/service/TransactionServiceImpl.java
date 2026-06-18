package com.machado.bank.service;


import com.machado.bank.exception.ResourceNotFoundException;
import com.machado.bank.model.*;
import com.machado.bank.repository.AccountRepository;
import com.machado.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    private void validateOwnership(Account account, Long clientId) {
        if (!account.getClient().getId().equals(clientId)) {
            throw new SecurityException("No tienes acceso a esta cuenta");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }

    private Account findAccount(String number) {
        return accountRepository.findByAccountNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta " + number + " no encontrada"));
    }

    private void validateAccountActive(Account account) {
        if (!account.isActive()) { // se usa metodo de la entidad
            throw new IllegalStateException("La cuenta " + account.getAccountNumber() + " no está activa");
        }
    }

    private void validateAccountNotClosed(Account account) {
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "La cuenta " + account.getAccountNumber() + " está cerrada y no puede recibir transferencias"
            );
        }
    }

    // --- MÉTODOS DE IMPLEMENTACIÓN ---
    @Override
    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, Long clientId) {

        validateAmount(amount);
        Account account = findAccount(accountNumber);
        validateOwnership(account, clientId);
        validateAccountNotClosed(account);
        //validateAccountActive(account);

        // La entidad actualiza su saldo internamente
        account.credit(amount);

        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                account.getBalance(),
                "Depósito en cuenta",
                null,
                account
        );

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount, Long clientId) {

        validateAmount(amount);
        Account account = findAccount(accountNumber);

        validateOwnership(account, clientId);
        validateAccountActive(account);

        // La entidad valida fondos suficientes / Estado de cuenta / Resta el saldo
        account.debit(amount);

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAW,
                amount,
                account.getBalance(),
                "Retiro de cuenta",
                null,
                account
        );

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccount(String accountNumber, Long clientId) {

        Account account = findAccount(accountNumber);

        validateOwnership(account, clientId);

        return transactionRepository.findTop10ByAccountIdOrderByCreatedAtDesc(account.getId());
    }

    @Override
    @Transactional
    public String transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, Long clientId) {

        // 1. Validaciones rapidas (disparan IllegalArgumentException -> 400 Bad Request)
        validateAmount(amount);

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("No puede transferirse a la misma cuenta");
        }

        // 2. Obtención de datos (disparan ResourceNotFoundException -> 404 Not Found)
        Account from = findAccount(fromAccountNumber);
        Account to = findAccount(toAccountNumber);

        // SOLO VALIDAMOS LA CUENTA ORIGEN
        validateOwnership(from, clientId);

        // 3. Validaciones de estado Origen => active Destino => != Closed
        validateAccountActive(from);
        validateAccountNotClosed(to);

        // 4. Logica de saldos (dispara IllegalStateException si no hay fondos -> 400 Bad Request)
        from.debit(amount);
        to.credit(amount);

        // 5. Registro de auditoria
        String reference = UUID.randomUUID().toString();

        transactionRepository.save(new Transaction(
                TransactionType.TRANSFER_OUT,
                amount,
                from.getBalance(),
                "Transferencia enviada a " + toAccountNumber,
                reference,
                from
        ));

        transactionRepository.save(new Transaction(
                TransactionType.TRANSFER_IN,
                amount,
                to.getBalance(),
                "Transferencia recibida de " + fromAccountNumber,
                reference,
                to
        ));

        // Si aqui ocurre un choque de versiones, el GlobalExceptionHandler
        // atrapara la ObjectOptimisticLockingFailureException automaticamente.
        return reference;

    }

    @Override
    public List<Transaction> findByReference(String referenceCode, Long clientId) {

        List<Transaction> transactions = transactionRepository.findByReferenceCode(referenceCode);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("Referencia no encontrada");
        }

        // VALIDAR QUE TODAS LAS TRANSACCIONES PERTENECEN AL CLIENTE
        for (Transaction tx : transactions) {
            if (!tx.getAccount().getClient().getId().equals(clientId)) {
                throw new SecurityException("No tienes acceso a esta referencia");
            }
        }

        return transactions;
    }

}

