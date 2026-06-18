package com.machado.bank.service;

import com.machado.bank.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {

    List<Client> findAll();
    Page<Client> findAllPaged(Pageable pageable);
    Client findById(Long id);
    Client findByDocumentNumber(String documentNumber);
    Client create(Client client);
    Client update(Long id, Client client);
    Client blockClient(Long id);
    Client closeClient(Long id);
    Client activateClient(Long id);
    void deleteById(Long id);

}
