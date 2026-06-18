package com.machado.bank.repository;


import com.machado.bank.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest Configura Hibernate - Configura repositorios - Rollback automático al final del test
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
    @Autowired  //Spring inyecta el repo real
    private ClientRepository clientRepository;

    @Test
    void shouldSaveAndFindClientByDocumentNumber() {

        // GIVEN -> datos
        Client client = new Client(
                "Jhon Machado",
                "123456789",
                "jhon@machadobank.com"
        );

        clientRepository.save(client);

        // WHEN -> accion
        Optional<Client> result =
                clientRepository.findByDocumentNumber("123456789");

        // THEN -> validacion
        assertThat(result).isPresent();
        assertThat(result.get().getEmail())
                .isEqualTo("jhon@machadobank.com");
    }
}
