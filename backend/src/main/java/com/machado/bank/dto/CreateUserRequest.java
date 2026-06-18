package com.machado.bank.dto;

import com.machado.bank.model.Role;

public record CreateUserRequest(
        String userName,
        String password,
        Role role
) {}
