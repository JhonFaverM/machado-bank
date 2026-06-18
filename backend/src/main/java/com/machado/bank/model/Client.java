package com.machado.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @JsonIgnore
    @OneToOne(mappedBy = "client")
    private User user;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE; // ACTIVE, BLOCKED, CLOSED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructor vacío (OBLIGATORIO para JPA)
    public Client() {}

    // Constructor que usa el TEST (manual)
    public Client(String fullName, String documentNumber, String email) {
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    // Cambiar estado de forma controlada

    public void block(){
        this.status = ClientStatus.BLOCKED;
    }

    public void close(){
        this.status = ClientStatus.CLOSED;
    }

    public void activate() {
        this.status = ClientStatus.ACTIVE;
    }

    public boolean IsActive(){
        return this.status == ClientStatus.ACTIVE;
    }

}
