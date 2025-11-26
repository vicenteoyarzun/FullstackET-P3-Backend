// ProductController.java - CORREGIDO
package com.tiendamascotas.controllers;

import com.tiendamascotas.entities.Product;
import com.tiendamascotas.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> productOpt = productRepository.findById(id);
        
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setNameProduct(productDetails.getNameProduct());
            product.setProductDescription(productDetails.getProductDescription());
            product.setPriceProduct(productDetails.getPriceProduct());
            product.setStockQuantity(productDetails.getStockQuantity());
            product.setCategoria(productDetails.getCategoria());
            product.setImageLink(productDetails.getImageLink());
            
            return ResponseEntity.ok(productRepository.save(product));
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/categoria/{categoria}")
    public List<Product> getProductsByCategory(@PathVariable String categoria) {
        return productRepository.findByCategoria(categoria);
    }

    @GetMapping("/buscar/{nameProduct}")
    public List<Product> searchProducts(@PathVariable String nameProduct) {
        return productRepository.findBynameProductContainingIgnoreCase(nameProduct);
    }
}