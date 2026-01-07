package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); // Phân trang


    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.name) " +
            "LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) " +
            "LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    @EntityGraph(attributePaths  ={"category", "productImages"})
    Optional<Product> findById(Long id);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    @EntityGraph(attributePaths  ={"category", "productImages"})
    List<Product> findProductsByIds(@Param("productIds") List<Long> productIds);



}
