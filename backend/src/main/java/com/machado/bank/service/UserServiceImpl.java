package com.machado.bank.service;

import com.machado.bank.dto.RegisterRequest;
import com.machado.bank.model.Client;
import com.machado.bank.model.Role;
import com.machado.bank.model.User;
import com.machado.bank.repository.ClientRepository;
import com.machado.bank.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ClientRepository clientRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Uso Admin
    @Override
    public User createUser(String userName, String password, Role role){

        if (userRepository.existsByUserName(userName)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    // REGISTRO (CLIENTE + USER)
    @Transactional
    public void register(RegisterRequest request) {

        // Validaciones
        if (userRepository.existsByUserName(request.userName())) {
            throw new RuntimeException("Username already exists");
        }

        if (clientRepository.findByDocumentNumber(request.documentNumber()).isPresent()) {
            throw new RuntimeException("Document already exists");
        }

        // Crear Client
        Client client = new Client(
                request.fullName(),
                request.documentNumber(),
                request.email()
        );

        clientRepository.save(client);

        // Crear User
        User user = new User();
        user.setUserName(request.userName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.CLIENT);
        user.setClient(client);
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
