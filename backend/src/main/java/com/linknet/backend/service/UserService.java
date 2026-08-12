package com.linknet.backend.service;

import com.linknet.backend.entity.User;
import com.linknet.backend.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRep userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserService(UserRep userRepository){
    this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("User id " + id + " not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Email not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User editUser(Integer id, User updatedFields) {
        User existingUser = findUserById(id);

        if (updatedFields.getFullName() != null)
            existingUser.setFullName(updatedFields.getFullName());
        if (updatedFields.getBio() != null)
            existingUser.setBio(updatedFields.getBio());
        if (updatedFields.getPassword() != null)
            existingUser.setPassword(updatedFields.getPassword());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }
}