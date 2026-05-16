package com.example.honey_stock.repository;

import com.example.honey_stock.entity.Product;
import com.example.honey_stock.entity.Sale;
import com.example.honey_stock.entity.Warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByProduct_NameContainingOrWarehouse_NameContainingOrWarehouse_RegionContainingOrMemoContaining(
            String productName,
            String warehouseName,
            String region,
            String memo
    );

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByProduct(Product product);
    boolean existsByWarehouse(Warehouse warehouse);
}