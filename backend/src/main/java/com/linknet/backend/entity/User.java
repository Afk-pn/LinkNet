package com.linknet.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;
    
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    
    @Column(length = 1000)
    private String bio;

    
    public User() {}

    
    public User(String username, String email, String fullName, String bio, String password) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
        this.password=password;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}


