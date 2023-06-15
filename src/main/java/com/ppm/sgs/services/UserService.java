package com.ppm.sgs.services;

import java.util.List;

import com.ppm.sgs.models.User;

public interface UserService {
    List<User> getAllUsers();

    List<User> getPendingUsers();

    List<User> getVerifiedUsers();

    User getById(String id);

    List<User> searchByIdOrName(String id, String name);

    String save(User user);

    String update(User user);

    void deleteById(String id);
}
