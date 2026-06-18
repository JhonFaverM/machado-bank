package com.machado.bank.repository;

import com.machado.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    //Spring, dame TODAS las operaciones CRUD y ademas estas consultas extra

    Optional<Client> findByDocumentNumber(String documentNumber);

    Optional<Client> findByEmail(String email);

}
