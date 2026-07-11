package com.machado.bank.repository;

import com.machado.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE BINARY user_name = :userName", nativeQuery = true)
    Optional<User> findByUserName(@Param("userName") String userName);

    boolean existsByUserName(String userName);
}
