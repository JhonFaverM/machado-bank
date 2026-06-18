package com.machado.bank.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia única para auditoría
    @Column(nullable = false, unique = false, updatable = false)
    private String referenceCode;

    // Tipo de transacción
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Monto
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // Saldo luego del movimiento
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    // Descripción opcional
    private String description;

    // Fecha automática
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Cuenta asociada
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {}

    public Transaction(TransactionType type,
                       BigDecimal amount,
                       BigDecimal balanceAfter,
                       String description,
                       String referenceCode,
                       Account account) {

        this.referenceCode = referenceCode;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.account = account;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.referenceCode == null) {
            this.referenceCode = UUID.randomUUID().toString();
        }
    }

    public Long getId() {
        return id;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Account getAccount() {
        return account;
    }

}
