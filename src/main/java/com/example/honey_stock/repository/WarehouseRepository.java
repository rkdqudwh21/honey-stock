package com.example.honey_stock.repository;

import com.example.honey_stock.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByNameContainingOrRegionContainingOrAddressContaining(
            String name,
            String region,
            String address
    );
}