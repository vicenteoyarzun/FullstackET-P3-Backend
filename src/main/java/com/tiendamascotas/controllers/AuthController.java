package com.tiendamascotas.controllers;

import com.tiendamascotas.entities.UserLogin;
import com.tiendamascotas.entities.Users;
import com.tiendamascotas.entities.UserType;
import com.tiendamascotas.repositories.UserLoginRepository;
import com.tiendamascotas.repositories.UsersRepository;
import com.tiendamascotas.repositories.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<UserLogin> userLoginOpt = userLoginRepository.findByUsername(loginRequest.getUsername());
            
            if (userLoginOpt.isPresent()) {
                UserLogin userLogin = userLoginOpt.get();
                
                if (userLogin.getPassword().equals(loginRequest.getPassword())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login exitoso");
                    
                    Optional<Users> userOpt = usersRepository.findById(userLogin.getId());
                    Optional<UserType> userTypeOpt = userTypeRepository.findById(userOpt.get().getUserTypeId());
                    
                    response.put("userId", userLogin.getId());
                    response.put("username", userLogin.getUsername());
                    response.put("email", userLogin.getEmail());
                    
                    if (userOpt.isPresent()) {
                        Users user = userOpt.get();
                        response.put("nombre", user.getNombre());
                        response.put("apellido", user.getApellidoPaterno());
                    }
                    
                    if (userTypeOpt.isPresent()) {
                        UserType userType = userTypeOpt.get();
                        response.put("userType", userType.getId());
                        response.put("tipoUsuario", userType.getTipo());
                        response.put("isAdmin", userType.getId().equals(1L));
                    } else {
                        response.put("userType", 3);
                        response.put("isAdmin", false);
                    }
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Credenciales inválidas");
            return ResponseEntity.status(401).body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en el servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (userLoginRepository.existsByUsername(registerRequest.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.status(400).body(errorResponse);
            }

            if (userLoginRepository.existsByEmail(registerRequest.getEmail())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "El email ya está registrado");
                return ResponseEntity.status(400).body(errorResponse);
            }

            Long nextUserId = getNextUserId();
            
            Users newUser = new Users();
            newUser.setId(nextUserId);
            newUser.setFname(registerRequest.getNombre());
            newUser.setFsurname(registerRequest.getApellidoPaterno());
            newUser.setSsurname(registerRequest.getApellidoMaterno());
            newUser.setRun(registerRequest.getRun());
            newUser.setDv(registerRequest.getDv());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setUserTypeId(3L);
            
            usersRepository.save(newUser);

            UserLogin newUserLogin = new UserLogin();
            newUserLogin.setId(nextUserId);
            newUserLogin.setEmail(registerRequest.getEmail());
            newUserLogin.setUsername(registerRequest.getUsername());
            newUserLogin.setPassword(registerRequest.getPassword());
            
            userLoginRepository.save(newUserLogin);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("userId", nextUserId);
            response.put("username", registerRequest.getUsername());
            response.put("userType", 3);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en el registro: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private Long getNextUserId() {
        return userLoginRepository.count() + 1;
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private Long run;
        private String dv;
        private String email;
        private String username;
        private String password;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
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
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}