package com.ppm.sgs.services;

import java.util.List;

import com.ppm.sgs.models.Role;

public interface RoleService {
    List<Role> getAllRoles();

    List<Role> getRolesByIds(List<Integer> ids);
    
}
