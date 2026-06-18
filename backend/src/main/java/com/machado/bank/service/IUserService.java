package com.machado.bank.service;

import com.machado.bank.dto.RegisterRequest;
import com.machado.bank.model.Role;
import com.machado.bank.model.User;

import java.util.Optional;

public interface IUserService {

    User createUser(String userName, String password, Role role);

    void register(RegisterRequest request);

    void disableUser(Long userId);

    void enableUser(Long userId);

    Optional<User> findByUserName(String userName);

}
