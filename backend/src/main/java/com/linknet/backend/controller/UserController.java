package com.linknet.backend.controller;

import com.linknet.backend.entity.User;
import com.linknet.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    public User editUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.editUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}