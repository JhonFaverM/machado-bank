package com.machado.bank.service;

import com.machado.bank.exception.DuplicateResourceException;
import com.machado.bank.exception.ResourceNotFoundException;
import com.machado.bank.model.Client;
import com.machado.bank.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;    //clientRepository = Atributo o Variable de Instancia

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client create(Client client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()){
            throw new DuplicateResourceException("Email ya está registrado");
        }

        if (clientRepository.findByDocumentNumber(client.getDocumentNumber()).isPresent()){
            throw new DuplicateResourceException("Documento pertenece a otro cliente");
        }
        return clientRepository.save(client);
    }

    // Metodo para bloquear un cliente
    @Override
    public Client blockClient(Long id) {
        Client client = findById(id);
        client.block();
        System.out.println("LOG de SEGURIDAD: Cliente ID " + id + " bloqueado.");
        return clientRepository.save(client);
    }

    // Metodo para cerrar relacion de cliente
    @Override
    public Client closeClient(Long id) {
        Client client = findById(id);
        client.close();
        System.out.println("LOG de SEGURIDAD: Cliente ID " + id + " ha sido cerrado.");
        return clientRepository.save(client);
    }

    @Override
    public Client activateClient(Long id) {
        Client client = findById(id);
        client.activate();
        System.out.println("LOG de SEGURIDAD: Cliente ID " + id + " nuevamente a sido activado.");
        return clientRepository.save(client);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Page<Client> findAllPaged(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    public Client findByDocumentNumber(String documentNumber) {
        return clientRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con documento "  + documentNumber + " no encontrado"));
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id:" + id));
    }


    @Override
    public Client update(Long id, Client client) {
        Client existing = findById(id);
        existing.setFullName(client.getFullName());
        existing.setEmail(client.getEmail());
        return clientRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        Client client = findById(id); // ya valida
        clientRepository.delete(client);
    }
}
