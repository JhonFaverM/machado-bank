package com.machado.bank.controller;

import com.machado.bank.model.Client;
import com.machado.bank.service.IClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("/admin/clients")
public class ClientController {

    private final IClientService service;

    public ClientController(IClientService service){
        this.service = service;
    }

    //Create Client (opcional / administrativo)
    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client){
        return ResponseEntity.ok(service.create(client));
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Buscar por documento
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<Client> getByDocumentNumber(@PathVariable String documentNumber){
        return ResponseEntity.ok(service.findByDocumentNumber(documentNumber));
    }

    // Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id,
                                         @RequestBody Client client) {
        return ResponseEntity.ok(service.update(id, client));
    }

    // Bloquear cliente
    @PatchMapping("/{id}/block")
    public ResponseEntity<Map<String, String>> block(@PathVariable Long id) {
        Client client = service.blockClient(id);
        return ResponseEntity.ok(Map.of(
                "message", "Cliente " + client.getFullName() + " bloqueado",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Activar cliente
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Map<String, String>> activate(@PathVariable Long id) {
        Client client = service.activateClient(id);
        return ResponseEntity.ok(Map.of(
                "message", "Cliente " + client.getFullName() + " activado",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Paginación
    @GetMapping
    public ResponseEntity<Page<Client>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    // Cerrar cliente
    @PatchMapping("/{id}/close")
    public ResponseEntity<Map<String, String>> close(@PathVariable Long id) {
        Client client = service.closeClient(id);
        return ResponseEntity.ok(Map.of(
                "message", "Relación comercial con el Cliente " + client.getFullName() + " ha sido CERRADA",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Eliminar cliente (opcional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
