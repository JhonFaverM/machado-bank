package com.machado.bank.controller;

import com.machado.bank.model.Account;
import com.machado.bank.model.Client;
import com.machado.bank.service.AuthService;
import com.machado.bank.service.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService accountService;
    private final AuthService authService;

    public AccountController(IAccountService accountService,
                             AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @PostMapping("/my-account")
    public ResponseEntity<Account> createAccount() {

        Client client = authService.getAuthenticatedClient();

        Account account = accountService.createAccount(client.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getMyAccount(
            @PathVariable String accountNumber) {

        Client client = authService.getAuthenticatedClient();

        Account account = accountService.findByAccountNumber(
                accountNumber,
                client.getId()
        );
        return ResponseEntity.ok(account);
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<Account>> getMyAccounts() {

        Client client = authService.getAuthenticatedClient();

        List<Account> accounts = accountService.findByClient(client.getId());

        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/{accountNumber}/block")
    public ResponseEntity<Map<String, String>> block(
            @PathVariable String accountNumber) {

        Client client = authService.getAuthenticatedClient();

        Account account = accountService.blockAccount(
                accountNumber,
                client.getId()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " bloqueada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    @PatchMapping("/{accountNumber}/activate")
    public ResponseEntity<Map<String, String>> activate(
            @PathVariable String accountNumber) {

        Client client = authService.getAuthenticatedClient();

        Account account = accountService.activateAccount(
                accountNumber,
                client.getId()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " activada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<Map<String, String>> close(
            @PathVariable String accountNumber) {

        Client client = authService.getAuthenticatedClient();

        Account account = accountService.closeAccount(
                accountNumber,
                client.getId()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " cerrada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

}
