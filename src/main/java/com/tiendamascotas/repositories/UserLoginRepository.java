package com.tiendamascotas.repositories;

import com.tiendamascotas.entities.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    Optional<UserLogin> findByUsername(String username);
    Optional<UserLogin> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}