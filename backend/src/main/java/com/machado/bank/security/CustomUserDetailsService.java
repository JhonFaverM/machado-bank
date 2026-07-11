package com.machado.bank.security;

import com.machado.bank.model.User;
import com.machado.bank.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String authority = "ROLE_" + user.getRole().name();

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(authority)
                .disabled(!user.isEnabled())
                .build();
    }
}