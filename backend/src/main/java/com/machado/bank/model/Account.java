package com.machado.bank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "accounts")
public class Account {

    @Id //LLave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Le dice a MySQL, encarguese del auto_increm
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version    //Evita que dos clientes cambien el saldo al mismo tiempo
    @Column(nullable = false)
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE; // ACTIVE, BLOCKED, CLOSED

    @OneToOne  // Un cliente una cuenta
    @JoinColumn(name = "client_id", nullable = false, unique = true)   //Crea una llave foranea en la tabla accounts. en esta columna se guarda el ID del cliente para sabe de quien es la cuenta.
    private Client client;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Account() {}

    public Account(String accountNumber, Client client) {
        this.accountNumber = accountNumber;
        this.client = client;
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //==========================
    //Operaciones de dinero
    //==========================

    // Suma un monto al saldo actual
    public void credit(BigDecimal amount) {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "No se puede transferir a una cuenta en estado: " + this.status
            );
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser positivo");
        }

        this.balance = this.balance.add(amount);
    }


    // Resta un monto al saldo actual.
    public void debit(BigDecimal amount) {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "No se puede acreditar en una cuenta cerrada"
            );
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "Fondos insuficientes en la cuenta: " + this.accountNumber
            );
        }
        this.balance = this.balance.subtract(amount);
    }

    // =========================
    // VALIDACIONES INTERNAS
    // =========================


    // Getters & Setters
    public Long getId() { return id; }

    public String getAccountNumber() { return accountNumber; }

    public BigDecimal getBalance() { return balance; }

    public AccountStatus getStatus() { return status; }

    public Client getClient() { return client; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // Metodo de consulta
    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    // cambiar estado de forma controlada

    public void block() {
        this.status = AccountStatus.BLOCKED;
    }

    public void activate() {
        this.status = AccountStatus.ACTIVE;
    }

    public void close() {
        this.status = AccountStatus.CLOSED;
    }

    private void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

}
