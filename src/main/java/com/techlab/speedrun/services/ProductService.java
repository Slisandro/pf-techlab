package com.techlab.speedrun.services;

import com.techlab.speedrun.entity.Product;
import com.techlab.speedrun.repository.ProductRepository;
import com.techlab.speedrun.utilities.StringUtilities;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final StringUtilities stringUtils;

    public ProductService(ProductRepository productRepository, StringUtilities stringUtils) {
        this.productRepository = productRepository;
        this.stringUtils = stringUtils;
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        product.setDeleted(false);
        log.info("Creating new product: {}", product.getName());
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    public List<Product> findAllProducts(String name, String category) {
        if (bothParamsPresent(name, category)) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(name, category);
        }
        
        if (stringUtils.isEmpty(name) && stringUtils.isEmpty(category)) {
            return productRepository.findAll();
        }
        
        if (!stringUtils.isEmpty(name)) {
            return productRepository.findByNameContainingIgnoreCase(name);
        }
        
        return productRepository.findByCategoryContainingIgnoreCase(category);
    }

    
    public List<Product> findProductsByCategory(String category) {
        if (stringUtils.isEmpty(category)) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        log.info("Found {} products in category: {}", products.size(), category);
        return products;
    }
    
    public List<Product> findActiveProductsByCategory(String category) {
        if (stringUtils.isEmpty(category)) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        
        List<Product> products = productRepository.findActiveProductsByCategory(category);
        log.info("Found {} active products in category: {}", products.size(), category);
        return products;
    }
    
    public List<String> getAllCategories() {
        List<String> categories = productRepository.findAllDistinctCategories();
        log.info("Found {} distinct categories", categories.size());
        return categories.stream()
                .filter(cat -> !stringUtils.isEmpty(cat))
                .sorted()
                .collect(Collectors.toList());
    }
    
    public Map<String, Long> getProductCountByCategory() {
        List<Object[]> results = productRepository.countProductsByCategory();
        return results.stream()
                .collect(Collectors.toMap(
                    obj -> (String) obj[0],
                    obj -> (Long) obj[1]
                ));
    }
    
    public List<Product> findProductsByCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Categories list cannot be empty");
        }
        
        return categories.stream()
                .filter(cat -> !stringUtils.isEmpty(cat))
                .flatMap(cat -> productRepository.findByCategoryIgnoreCase(cat).stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<String> searchCategories(String searchTerm) {
        if (stringUtils.isEmpty(searchTerm)) {
            return getAllCategories();
        }
        
        return getAllCategories().stream()
                .filter(cat -> cat.toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Product updateProduct(Long id, Product updatedData) {
        Product existingProduct = getProductById(id);
        
        if (!stringUtils.isEmpty(updatedData.getName())) {
            log.info("Updating product name from '{}' to '{}'", 
                     existingProduct.getName(), updatedData.getName());
            existingProduct.setName(updatedData.getName());
        }
        
        if (updatedData.getPrice() != null && updatedData.getPrice() > 0) {
            existingProduct.setPrice(updatedData.getPrice());
        }
        
        if (!stringUtils.isEmpty(updatedData.getDescription())) {
            existingProduct.setDescription(updatedData.getDescription());
        }
        
        if (!stringUtils.isEmpty(updatedData.getCategory())) {
            existingProduct.setCategory(updatedData.getCategory());
        }
        
        if (updatedData.getStock() != null) {
            existingProduct.setStock(updatedData.getStock());
        }
        
        return productRepository.save(existingProduct);
    }

    public Product softDeleteProduct(Long id) {
        Product product = getProductById(id);
        
        if (Boolean.TRUE.equals(product.getDeleted())) {
            throw new IllegalStateException("Product is already deleted");
        }
        
        product.setDeleted(true);
        log.info("Soft deleting product with ID: {}", id);
        
        return productRepository.save(product);
    }

    public List<Product> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .collect(Collectors.toList());
    }

    private void validateProduct(Product product) {
        if (stringUtils.isEmpty(product.getName())) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        
        if (product.getStock() != null && product.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }
    
    private boolean bothParamsPresent(String name, String category) {
        return !stringUtils.isEmpty(name) && !stringUtils.isEmpty(category);
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}