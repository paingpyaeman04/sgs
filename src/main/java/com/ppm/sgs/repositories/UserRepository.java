package com.ppm.sgs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppm.sgs.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    List<User> findByEnabled(Boolean isEnabled);

    List<User> findByIdOrName(String id, String name);

    Optional<User> findFirstByOrderByIdDesc();

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
