package com.tiendamascotas.controllers;

import com.tiendamascotas.entities.Product;
import com.tiendamascotas.entities.UserLogin;
import com.tiendamascotas.repositories.ProductRepository;
import com.tiendamascotas.repositories.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendedor")
@CrossOrigin(origins = "http://localhost:3000")
public class VendedorController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @GetMapping("/productos/todos")
    public ResponseEntity<?> getAllProductos(@RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            List<Product> todosProductos = productRepository.findAll();
            
            return ResponseEntity.ok(createSuccessResponse("Todos los productos obtenidos", todosProductos));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/productos/mis-productos")
    public ResponseEntity<?> getMisProductos(@RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            Long idVendedor = vendedorOpt.get().getId();
            List<Product> misProductos = productRepository.findByIdVendedor(idVendedor);
            
            return ResponseEntity.ok(createSuccessResponse("Productos obtenidos", misProductos));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody ProductRequest request, 
                                         @RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            Long idVendedor = vendedorOpt.get().getId();
            
            if (request.getNameProduct().length() > 30) {
                return ResponseEntity.badRequest().body(createErrorResponse("El nombre no puede exceder 30 caracteres"));
            }
            
            Product nuevoProducto = new Product();
            nuevoProducto.setNameProduct(request.getNameProduct());
            nuevoProducto.setProductDescription(request.getProductDescription());
            nuevoProducto.setPriceProduct(request.getPriceProduct());
            nuevoProducto.setStockQuantity(request.getStockQuantity());
            nuevoProducto.setCategoria(request.getCategoria());
            nuevoProducto.setImageLink(request.getImageLink());
            nuevoProducto.setIdVendedor(idVendedor);
            
            Product productoGuardado = productRepository.save(nuevoProducto);
            
            return ResponseEntity.ok(createSuccessResponse("Producto creado exitosamente", productoGuardado));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error al crear producto: " + e.getMessage()));
        }
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, 
                                              @RequestBody ProductRequest request,
                                              @RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            Long idVendedor = vendedorOpt.get().getId();
            
            Optional<Product> productoOpt = productRepository.findById(id);
            if (productoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Producto no encontrado"));
            }
            
            Product producto = productoOpt.get();
            if (!idVendedor.equals(producto.getIdVendedor())) {
                return ResponseEntity.badRequest().body(createErrorResponse("No tienes permisos para modificar este producto"));
            }
            
            if (request.getNameProduct().length() > 30) {
                return ResponseEntity.badRequest().body(createErrorResponse("El nombre no puede exceder 30 caracteres"));
            }
            
            producto.setNameProduct(request.getNameProduct());
            producto.setProductDescription(request.getProductDescription());
            producto.setPriceProduct(request.getPriceProduct());
            producto.setStockQuantity(request.getStockQuantity());
            producto.setCategoria(request.getCategoria());
            producto.setImageLink(request.getImageLink());
            
            Product productoActualizado = productRepository.save(producto);
            
            return ResponseEntity.ok(createSuccessResponse("Producto actualizado exitosamente", productoActualizado));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error al actualizar producto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id,
                                            @RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            Long idVendedor = vendedorOpt.get().getId();
            
            Optional<Product> productoOpt = productRepository.findById(id);
            if (productoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Producto no encontrado"));
            }
            
            Product producto = productoOpt.get();
            if (!idVendedor.equals(producto.getIdVendedor())) {
                return ResponseEntity.badRequest().body(createErrorResponse("No tienes permisos para eliminar este producto"));
            }
            
            productRepository.deleteById(id);
            
            return ResponseEntity.ok(createSuccessResponse("Producto eliminado exitosamente", null));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error al eliminar producto: " + e.getMessage()));
        }
    }

    @GetMapping("/dashboard/estadisticas")
    public ResponseEntity<?> getEstadisticas(@RequestHeader("Authorization") String username) {
        try {
            Optional<UserLogin> vendedorOpt = userLoginRepository.findByUsername(username);
            if (vendedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Vendedor no encontrado"));
            }
            
            Long idVendedor = vendedorOpt.get().getId();
            
            List<Product> misProductos = productRepository.findAll()
                    .stream()
                    .filter(product -> idVendedor.equals(product.getIdVendedor()))
                    .collect(Collectors.toList());
            
            long totalProductos = misProductos.size();
            long stockBajo = misProductos.stream().filter(p -> p.getStockQuantity() < 10).count();
            BigDecimal valorInventario = misProductos.stream()
                    .map(p -> p.getPriceProduct().multiply(BigDecimal.valueOf(p.getStockQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalProductos", totalProductos);
            estadisticas.put("stockBajo", stockBajo);
            estadisticas.put("valorInventario", valorInventario);
            estadisticas.put("productos", misProductos.size());
            
            return ResponseEntity.ok(createSuccessResponse("Estadísticas obtenidas", estadisticas));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Error: " + e.getMessage()));
        }
    }

    public static class ProductRequest {
        private String nameProduct;
        private String productDescription;
        private BigDecimal priceProduct;
        private Integer stockQuantity;
        private String categoria;
        private String imageLink;

        public String getNameProduct() { return nameProduct; }
        public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
        
        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
        
        public BigDecimal getPriceProduct() { return priceProduct; }
        public void setPriceProduct(BigDecimal priceProduct) { this.priceProduct = priceProduct; }
        
        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getImageLink() { return imageLink; }
        public void setImageLink(String imageLink) { this.imageLink = imageLink; }
    }

    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}