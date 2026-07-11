package com.machado.bank.service;

import com.machado.bank.model.Client;
import com.machado.bank.model.User;
import com.machado.bank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAuthenticatedUsername() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return auth.getName();
    }

    public User getAuthenticatedUser() {

        String username = getAuthenticatedUsername();

        return userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new RuntimeException("Usuario autenticado no encontrado"));
    }

    public Client getAuthenticatedClient() {

        User user = getAuthenticatedUser();

        if (user.getClient() == null) {
            throw new RuntimeException("El usuario no tiene cliente asociado");
        }

        return user.getClient();
    }
}