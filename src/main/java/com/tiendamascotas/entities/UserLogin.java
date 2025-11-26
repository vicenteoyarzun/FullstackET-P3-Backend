package com.tiendamascotas.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "USER_LOGIN")
public class UserLogin {
    @Id
    @Column(name = "ID_USER")
    private Long id;
    
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "USERNAME")
    private String username;
    
    @Column(name = "USER_PASSWORD")
    private String password;
    
    // Constructores
    public UserLogin() {}
    
    public UserLogin(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}