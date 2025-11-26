package com.tiendamascotas.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class Users {
    @Id
    @Column(name = "ID_USER")
    private Long id;
    
    @Column(name = "FNAME")
    private String nombre;
    
    @Column(name = "SNAME")
    private String segundoNombre;
    
    @Column(name = "FSURNAME")
    private String apellidoPaterno;
    
    @Column(name = "SSURNAME")
    private String apellidoMaterno;
    
    @Column(name = "RUN")
    private Long run;
    
    @Column(name = "DV")
    private String dv;
    
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "PHONENUMBER")
    private Long phoneNumber;
    
    @Column(name = "ID_USER_TYPE")
    private Long userTypeId;
    
    public Users() {}
    
    public Users(String nombre, String apellidoPaterno, String email) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.email = email;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    
    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    
    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    
    public Long getRun() { return run; }
    public void setRun(Long run) { this.run = run; }
    
    public String getDv() { return dv; }
    public void setDv(String dv) { this.dv = dv; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Long phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Long getUserTypeId() { return userTypeId; }
    public void setUserTypeId(Long userTypeId) { this.userTypeId = userTypeId; }
    
    public String getFname() { return nombre; }
    public void setFname(String fname) { this.nombre = fname; }
    public String getFsurname() { return apellidoPaterno; }
    public void setFsurname(String fsurname) { this.apellidoPaterno = fsurname; }
    public String getSsurname() { return apellidoMaterno; }
    public void setSsurname(String ssurname) { this.apellidoMaterno = ssurname; }
}