package com.tiendamascotas.controllers;

import com.tiendamascotas.entities.*;
import com.tiendamascotas.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            
            long totalUsuarios = usersRepository.count();
            estadisticas.put("totalUsuarios", totalUsuarios);
            
            long totalProductos = productRepository.count();
            estadisticas.put("totalProductos", totalProductos);
            
            List<Product> productosBajoStock = productRepository.findByStockQuantityGreaterThan(0)
                .stream()
                .filter(p -> p.getStockQuantity() < 10)
                .collect(Collectors.toList());
            estadisticas.put("productosBajoStock", productosBajoStock.size());
            
            long vendedoresActivos = usersRepository.countByUserTypeId(2L);
            estadisticas.put("vendedoresActivos", vendedoresActivos);
            
            return ResponseEntity.ok(estadisticas);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> getAllUsuarios() {
        try {
            List<Users> usuarios = usersRepository.findAll();
            
            List<Map<String, Object>> usuariosData = usuarios.stream().map(usuario -> {
                Map<String, Object> usuarioData = new HashMap<>();
                usuarioData.put("id", usuario.getId());
                usuarioData.put("nombre", usuario.getNombre());
                usuarioData.put("apellidoPaterno", usuario.getApellidoPaterno());
                usuarioData.put("apellidoMaterno", usuario.getApellidoMaterno());
                usuarioData.put("email", usuario.getEmail());
                usuarioData.put("userTypeId", usuario.getUserTypeId());
                usuarioData.put("run", usuario.getRun());
                usuarioData.put("dv", usuario.getDv());
                
                userLoginRepository.findById(usuario.getId()).ifPresent(login -> {
                    usuarioData.put("username", login.getUsername());
                });
                
                userTypeRepository.findById(usuario.getUserTypeId()).ifPresent(userType -> {
                    usuarioData.put("tipoUsuario", userType.getTipo());
                });
                
                return usuarioData;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(usuariosData);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener usuarios: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/productos")
    public ResponseEntity<?> getAllProductos() {
        try {
            List<Product> productos = productRepository.findAll();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener productos: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody ProductRequest productRequest) {
        try {
            Product newProduct = new Product(
                productRequest.getNameProduct(),
                productRequest.getPriceProduct(),
                productRequest.getStockQuantity(),
                productRequest.getImageLink(),
                productRequest.getCategoria(),
                productRequest.getProductDescription(),
                null
            );
            
            productRepository.save(newProduct);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("productId", newProduct.getIdProduct());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al crear producto: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product producto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            producto.setNameProduct(productRequest.getNameProduct());
            producto.setPriceProduct(productRequest.getPriceProduct());
            producto.setStockQuantity(productRequest.getStockQuantity());
            producto.setImageLink(productRequest.getImageLink());
            producto.setCategoria(productRequest.getCategoria());
            producto.setProductDescription(productRequest.getProductDescription());
            
            productRepository.save(producto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar producto: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            if (!productRepository.existsById(id)) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Producto no encontrado");
                return ResponseEntity.status(404).body(error);
            }

            productRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al eliminar producto: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody UserRequest userRequest) {
        try {
            if (userLoginRepository.existsByUsername(userRequest.getUsername())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El nombre de usuario ya existe");
                return ResponseEntity.status(400).body(error);
            }

            if (userLoginRepository.existsByEmail(userRequest.getEmail())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "El email ya está registrado");
                return ResponseEntity.status(400).body(error);
            }

            Long nextUserId = getNextUserId();
            
            Users newUser = new Users();
            newUser.setId(nextUserId);
            newUser.setFname(userRequest.getNombre());
            newUser.setFsurname(userRequest.getApellidoPaterno());
            newUser.setSsurname(userRequest.getApellidoMaterno());
            newUser.setRun(userRequest.getRun());
            newUser.setDv(userRequest.getDv());
            newUser.setEmail(userRequest.getEmail());
            newUser.setUserTypeId(userRequest.getUserTypeId());
            
            usersRepository.save(newUser);

            UserLogin newUserLogin = new UserLogin();
            newUserLogin.setId(nextUserId);
            newUserLogin.setEmail(userRequest.getEmail());
            newUserLogin.setUsername(userRequest.getUsername());
            newUserLogin.setPassword(userRequest.getPassword());
            
            userLoginRepository.save(newUserLogin);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("userId", nextUserId);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al crear usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        try {
            Users usuario = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            usuario.setFname(userRequest.getNombre());
            usuario.setFsurname(userRequest.getApellidoPaterno());
            usuario.setSsurname(userRequest.getApellidoMaterno());
            usuario.setRun(userRequest.getRun());
            usuario.setDv(userRequest.getDv());
            usuario.setEmail(userRequest.getEmail());
            usuario.setUserTypeId(userRequest.getUserTypeId());
            
            usersRepository.save(usuario);

            UserLogin userLogin = userLoginRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Login de usuario no encontrado"));
            
            userLogin.setEmail(userRequest.getEmail());
            userLogin.setUsername(userRequest.getUsername());
            
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                userLogin.setPassword(userRequest.getPassword());
            }
            
            userLoginRepository.save(userLogin);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            if (!usersRepository.existsById(id)) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Usuario no encontrado");
                return ResponseEntity.status(404).body(error);
            }

            userLoginRepository.deleteById(id);
            usersRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    private Long getNextUserId() {
        return userLoginRepository.count() + 1;
    }

    public static class UserRequest {
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private Long run;
        private String dv;
        private String email;
        private String username;
        private String password;
        private Long userTypeId;

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
        public Long getUserTypeId() { return userTypeId; }
        public void setUserTypeId(Long userTypeId) { this.userTypeId = userTypeId; }
    }

    public static class ProductRequest {
        private String nameProduct;
        private BigDecimal priceProduct;
        private Integer stockQuantity;
        private String imageLink;
        private String categoria;
        private String productDescription;

        public String getNameProduct() { return nameProduct; }
        public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
        public BigDecimal getPriceProduct() { return priceProduct; }
        public void setPriceProduct(BigDecimal priceProduct) { this.priceProduct = priceProduct; }
        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
        public String getImageLink() { return imageLink; }
        public void setImageLink(String imageLink) { this.imageLink = imageLink; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    }
}