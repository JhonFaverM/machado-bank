package com.machado.bank.controller;

import com.machado.bank.model.Account;
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

    private final IAccountService service;
    private final AuthService authService;

    public AccountController(IAccountService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @PostMapping("/my-account")
    public ResponseEntity<Account> createAccount() {

        var client = authService.getAuthenticatedClient();

        Account account = service.createAccount(client.getId());

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    //Buscar cuenta por numero
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getMyAccount(@PathVariable String accountNumber) {
        var client = authService.getAuthenticatedClient();
        Account account = service.findByAccountNumber(accountNumber, client.getId());
        return ResponseEntity.ok(account);
    }

    //Listar cuentas
    @GetMapping("/my-accounts")
    public ResponseEntity<List<Account>> getMyAccounts() {

        var client = authService.getAuthenticatedClient();

        System.out.println("CLIENT ID: " + client.getId());

        List<Account> accounts = service.findByClient(client.getId());

        System.out.println("CUENTAS ENCONTRADAS: " + accounts.size());

        return ResponseEntity.ok(accounts);
    }

    //Bloquear cuenta (seguridad/fraude)
    // Bloquear cuenta
    @PatchMapping("/{accountNumber}/block")
    public ResponseEntity<Map<String, String>> block(@PathVariable String accountNumber) {
        var client = authService.getAuthenticatedClient();
        Account account = service.blockAccount(accountNumber, client.getId());

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " bloqueada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Activar cuenta
    @PatchMapping("/{accountNumber}/activate")
    public ResponseEntity<Map<String, String>> activate(@PathVariable String accountNumber) {
        var client = authService.getAuthenticatedClient();
        Account account = service.activateAccount(accountNumber, client.getId());

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " activada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Cerrar cuenta
    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<Map<String, String>> close(@PathVariable String accountNumber) {
        var client = authService.getAuthenticatedClient();
        Account account = service.closeAccount(accountNumber, client.getId());

        return ResponseEntity.ok(Map.of(
                "message", "Cuenta " + account.getAccountNumber() + " cerrada",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

}
