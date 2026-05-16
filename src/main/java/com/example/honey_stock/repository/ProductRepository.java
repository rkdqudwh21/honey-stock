package com.example.honey_stock.repository;

import com.example.honey_stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingOrSizeContainingOrDescriptionContaining(
            String name,
            String size,
            String description
    );
}