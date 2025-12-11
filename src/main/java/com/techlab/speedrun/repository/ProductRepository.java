package com.techlab.speedrun.repository;

import com.techlab.speedrun.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryContainingIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name, String category);
    
    List<Product> findByCategoryIgnoreCase(String category);
    
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
    List<String> findAllDistinctCategories();
    
    List<Product> findByCategoryIgnoreCaseAndDeletedFalse(String category);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category) AND p.deleted = false")
    List<Product> findActiveProductsByCategory(@Param("category") String category);
    
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.deleted = false GROUP BY p.category")
    List<Object[]> countProductsByCategory();
}