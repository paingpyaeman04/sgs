package com.ppm.sgs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppm.sgs.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findByEnabled(Boolean isEnabled, Pageable pageable);

    List<User> findByIdContaining(String id);

    List<User> findByNameContaining(String name);

    List<User> findByIdContainingAndNameContaining(String id, String name);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name LIKE %?1%")
    List<User> findUsersByRole(String role);

    Optional<User> findFirstByOrderByIdDesc();

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
