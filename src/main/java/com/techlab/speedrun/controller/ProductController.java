package com.techlab.speedrun.controller;

import com.techlab.speedrun.entity.Product;
import com.techlab.speedrun.services.ProductService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        List<Product> products = productService.findAllProducts(name, category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        List<Product> products = productService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productData) {
        Product updated = productService.updateProduct(id, productData);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        Product deleted = productService.softDeleteProduct(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.findProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{category}/active")
    public ResponseEntity<List<Product>> getActiveProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.findActiveProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/count")
    public ResponseEntity<Map<String, Long>> getProductCountByCategory() {
        Map<String, Long> countByCategory = productService.getProductCountByCategory();
        return ResponseEntity.ok(countByCategory);
    }
    
    @GetMapping("/categories/search")
    public ResponseEntity<List<String>> searchCategories(
            @RequestParam(required = false) String q) {
        List<String> categories = productService.searchCategories(q);
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/by-categories")
    public ResponseEntity<List<Product>> getProductsByCategories(@RequestBody List<String> categories) {
        List<Product> products = productService.findProductsByCategories(categories);
        return ResponseEntity.ok(products);
    }

    @ExceptionHandler(ProductService.ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductService.ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}