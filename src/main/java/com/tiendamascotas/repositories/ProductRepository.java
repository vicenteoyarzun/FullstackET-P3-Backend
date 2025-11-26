
package com.tiendamascotas.repositories;

import com.tiendamascotas.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoria(String categoria);
    List<Product> findBynameProductContainingIgnoreCase(String nameProduct);
    List<Product> findByIdVendedor(Long idVendedor);
    List<Product> findByStockQuantityGreaterThan(Integer stockQuantity);
}