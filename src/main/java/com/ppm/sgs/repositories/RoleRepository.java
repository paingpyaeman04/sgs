package com.ppm.sgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppm.sgs.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByIdIn(List<Integer> ids);
}
