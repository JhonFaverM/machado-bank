package com.machado.bank.controller;

import com.machado.bank.dto.TransactionRequestDTO;
import com.machado.bank.dto.TransferRequestDTO;
import com.machado.bank.model.Transaction;
import com.machado.bank.service.AuthService;
import com.machado.bank.service.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final ITransactionService transactionService;
    private final AuthService authService;

    public TransactionController(ITransactionService transactionService, AuthService authService) {
        this.transactionService = transactionService;
        this.authService = authService;
    }

    // *** DEPÓSITO ***
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody TransactionRequestDTO dto) {

        var client = authService.getAuthenticatedClient();

        Transaction tx = transactionService.deposit(
                dto.accountNumber(),
                dto.amount(),
                client.getId()
        );

        return ResponseEntity.ok(tx);
    }

    // *** RETIRO ***
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody TransactionRequestDTO dto) {

        var client = authService.getAuthenticatedClient();

        Transaction tx = transactionService.withdraw(
                dto.accountNumber(),
                dto.amount(),
                client.getId()
        );

        return ResponseEntity.ok(tx);
    }

    // *** TRANSFERENCIA ***
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@Valid @RequestBody TransferRequestDTO dto) {

        var client = authService.getAuthenticatedClient();

        String reference = transactionService.transfer(
                dto.fromAccountNumber(),
                dto.toAccountNumber(),
                dto.amount(),
                client.getId()
        );

        return ResponseEntity.ok(Map.of(
                "message", "Transferencia exitosa",
                "reference", reference
        ));
    }

    // *** HISTORIAL ***
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String accountNumber) {

        var client = authService.getAuthenticatedClient();

        List<Transaction> history = transactionService.findByAccount(
                accountNumber,
                client.getId());

        return ResponseEntity.ok(history);
    }

    @GetMapping("/reference/{referenceCode}")
    public ResponseEntity<List<Transaction>> getByReference(@PathVariable String referenceCode) {

        var client = authService.getAuthenticatedClient();

        List<Transaction> transactions = transactionService.findByReference(
                referenceCode,
                client.getId()
        );

        return ResponseEntity.ok(transactions);
    }

}
