package com.tiendamascotas.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "USER_TYPE")
public class UserType {
    @Id
    @Column(name = "ID_USER_TYPE")
    private Long id;
    
    @Column(name = "NAME_USER_TYPE")
    private String tipo;
    
    // Constructores
    public UserType() {}
    
    public UserType(String tipo) {
        this.tipo = tipo;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}