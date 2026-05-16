package com.example.honey_stock.repository;

import com.example.honey_stock.entity.Inventory;
import com.example.honey_stock.entity.Product;
import com.example.honey_stock.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);

    List<Inventory> findByProduct_NameContainingOrWarehouse_NameContainingOrWarehouse_RegionContaining(
            String productName,
            String warehouseName,
            String region
    );
    boolean existsByProduct(Product product);
    boolean existsByWarehouse(Warehouse warehouse);
}