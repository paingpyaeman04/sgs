package com.ppm.sgs.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ppm.sgs.models.User;

public interface UserService {
    List<User> getAllUsers();

    Page<User> getUsers(int currentPage);

    Page<User> getPendingUsers(int currentPage);

    Page<User> getVerifiedUsers(int currentPage);

    List<User> getLecturers();

    User getById(String id);

    List<User> searchByIdOrName(String id, String name);

    String save(User user);

    String update(User user);

    void deleteById(String id);
}
