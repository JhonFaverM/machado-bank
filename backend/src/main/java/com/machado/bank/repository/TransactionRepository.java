package com.machado.bank.repository;

import com.machado.bank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Buscar por referencia única de transacción
    //Optional<Transaction> findByReferenceCode(String referenceCode);


    // Historial por cuenta ordenado por fecha descendente
    Page<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId, Pageable pageable);

    // Buscar por rango de fechas
    List<Transaction> findByAccountIdAndCreatedAtBetween(
            Long accountId,
            LocalDateTime start,
            LocalDateTime end
    );

    // ultimos 10 movimientos
    List<Transaction> findTop10ByAccountIdOrderByCreatedAtDesc(Long accountId);


    List<Transaction> findByReferenceCode(String referenceCode);

}
