package com.ppm.sgs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ppm.sgs.exceptions.EmailAlreadyExistsException;
import com.ppm.sgs.exceptions.UserNotFoundException;
import com.ppm.sgs.exceptions.UsernameAlreadyExistsException;
import com.ppm.sgs.models.User;
import com.ppm.sgs.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getPendingUsers() {
        return userRepository.findByEnabled(false);
    }

    @Override
    public List<User> getVerifiedUsers() {
        return userRepository.findByEnabled(true);
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> searchByIdOrName(String id, String name) {
        return userRepository.findByIdOrName(id, name);
    }

    /**
     * Return the error message if there is any problem while saving the entity.
     * Return null if the save operation is successful.
     */
    @Override
    public String save(User user) {
        String lastId = "USR001";
        Optional<User> userOpt = userRepository.findFirstByOrderByIdDesc();
        if (userOpt.isPresent()) {
            int prevId = Integer.parseInt(userOpt.get().getId().substring(3));
            lastId = "USR" + String.format("%03d", prevId + 1);
        }
        user.setId(lastId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            if(userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists.");
            }

            if(userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists.");
            }

            userRepository.saveAndFlush(user);
        } catch (UsernameAlreadyExistsException e) {
            return e.getMessage();
        } catch (EmailAlreadyExistsException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Unknown error";
        }

        return null;
    }

    @Override
    public String update(User user) {
        try {
            if(userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists.");
            }

            if(userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists.");
            }

            User oldUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));
            BeanUtils.copyProperties(user, oldUser, "id");
            userRepository.saveAndFlush(oldUser);
        } catch (UsernameAlreadyExistsException e) {
            return e.getMessage();
        } catch (EmailAlreadyExistsException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Unknown error";
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

}
