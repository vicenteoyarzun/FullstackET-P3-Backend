
package com.tiendamascotas.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "ID_PRODUCT")
    private Long idProduct;
    
    @Column(name = "NAME_PRODUCT", length = 30, nullable = false)
    private String nameProduct;
    
    @Column(name = "PRICE_PRODUCT", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceProduct;
    
    @Column(name = "STOCK_QUANTITY", nullable = false)
    private Integer stockQuantity;
    
    @Column(name = "IMAGE_LINK", nullable = false, length = 200)
    private String imageLink;
    
    @Column(name = "PRODUCT_DESCRIPTION", length = 200)
    private String productDescription;
    
    @Column(name = "CATEGORIA", length = 100)
    private String categoria;
    
    @Column(name = "ID_VENDEDOR")
    private Long idVendedor;
    
    public Product() {}
    
    public Product(String nameProduct, BigDecimal priceProduct, Integer stockQuantity, 
                   String imageLink, String categoria, String productDescription, Long idVendedor) {
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.stockQuantity = stockQuantity;
        this.imageLink = imageLink;
        this.categoria = categoria;
        this.productDescription = productDescription;
        this.idVendedor = idVendedor;
    }
    
    // Getters y Setters SOLO para las columnas principales
    public Long getIdProduct() { return idProduct; }
    public void setIdProduct(Long idProduct) { this.idProduct = idProduct; }
    
    public String getNameProduct() { return nameProduct; }
    public void setNameProduct(String nameProduct) { this.nameProduct = nameProduct; }
    
    public BigDecimal getPriceProduct() { return priceProduct; }
    public void setPriceProduct(BigDecimal priceProduct) { this.priceProduct = priceProduct; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getImageLink() { return imageLink; }
    public void setImageLink(String imageLink) { this.imageLink = imageLink; }
    
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public Long getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Long idVendedor) { this.idVendedor = idVendedor; }
    
    
    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", nameProduct='" + nameProduct + '\'' +
                ", priceProduct=" + priceProduct +
                ", stockQuantity=" + stockQuantity +
                ", categoria='" + categoria + '\'' +
                ", idVendedor=" + idVendedor +
                '}';
    }
}