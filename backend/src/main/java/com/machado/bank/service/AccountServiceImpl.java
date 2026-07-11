package com.machado.bank.service;

import com.machado.bank.exception.ResourceNotFoundException;
import com.machado.bank.model.Account;
import com.machado.bank.model.Client;
import com.machado.bank.repository.AccountRepository;
import com.machado.bank.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements IAccountService {

    private static final Logger logger =
            LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    // Valida que la cuenta pertenezca al cliente autenticado.
    private void validateOwnership(Account account, Long clientId) {

        if (!account.getClient().getId().equals(clientId)) {
            throw new SecurityException("No tienes acceso a esta cuenta");
        }
    }

    @Override
    @Transactional
    public Account createAccount(Long clientId) {

        if (accountRepository.existsByClientId(clientId)) {
            logger.warn("El cliente {} ya posee una cuenta activa.", clientId);
            throw new IllegalStateException("Usted ya posee una cuenta activa en Machado Bank.");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no existe"));


        Account account = new Account(
                UUID.randomUUID().toString(),
                client
        );

        logger.info(
                "Cuenta {} creada para cliente {}",
                account.getAccountNumber(),
                clientId);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account blockAccount(String accountNumber, Long clientId) {

        Account account = findByAccountNumber(accountNumber, clientId);

        // la entidad controla el estado
        account.block();

        logger.warn("Cuenta {} bloqueada por cliente {}",
                accountNumber,
                clientId);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account activateAccount(String accountNumber, Long clientId) {

        Account account = findByAccountNumber(accountNumber, clientId);

        // La entidad encapsula el cambio de estado.
        account.activate();

        logger.info("Cuenta {} activada por cliente {}", accountNumber, clientId);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account closeAccount(String accountNumber, Long clientId) {

        Account account = findByAccountNumber(accountNumber, clientId);

        // La entidad encapsula el cambio de estado.
        account.close();

        logger.info("Cuenta {} cerrada por cliente {}",
                accountNumber,
                clientId);

        return accountRepository.save(account);
    }

    @Override
    public Account findByAccountNumber(String accountNumber, Long clientId) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cuenta no encontrada"));

        validateOwnership(account, clientId);

        return account;
    }

    @Override
    public List<Account> findByClient(Long clientId) {

        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Cliente no existe");
        }

        return accountRepository.findByClientId(clientId);
    }

    @Override
    public Account findById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cuenta no encontrada"));
    }
}