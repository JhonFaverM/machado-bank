package com.machado.bank.config;

import com.machado.bank.model.Role;
import com.machado.bank.model.User;
import com.machado.bank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            String username = "admin";
            String password = "admin123";

            if (userRepository.findByUserName(username).isEmpty()) {

                User admin = new User();
                admin.setUserName(username);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);

                userRepository.save(admin);

                System.out.println("🔥 ADMIN INICIAL CREADO");
            }
        };
    }
}
