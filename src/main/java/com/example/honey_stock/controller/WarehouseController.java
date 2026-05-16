package com.example.honey_stock.controller;

import com.example.honey_stock.entity.Warehouse;
import com.example.honey_stock.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.honey_stock.repository.InventoryRepository;
import com.example.honey_stock.repository.SaleRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;

  @GetMapping
public String list(@RequestParam(required = false) String keyword, Model model) {

    if (keyword != null && !keyword.isBlank()) {
        model.addAttribute("warehouses",
                warehouseRepository.findByNameContainingOrRegionContainingOrAddressContaining(
                        keyword, keyword, keyword
                ));
    } else {
        model.addAttribute("warehouses", warehouseRepository.findAll());
    }

    model.addAttribute("keyword", keyword);
    return "warehouse/list";
}

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "warehouse/form";
    }

    @PostMapping
    public String save(@ModelAttribute Warehouse warehouse) {
        warehouseRepository.save(warehouse);
        return "redirect:/warehouses";
    }
    @GetMapping("/{id}/edit")
public String editForm(@PathVariable Long id, Model model) {
    Warehouse warehouse = warehouseRepository.findById(id).orElseThrow();
    model.addAttribute("warehouse", warehouse);
    return "warehouse/edit";
}

@PostMapping("/{id}/edit")
public String update(@PathVariable Long id,
                     @RequestParam String name,
                     @RequestParam String region,
                     @RequestParam(required = false) String address) {

    Warehouse warehouse = warehouseRepository.findById(id).orElseThrow();

    warehouse.setName(name);
    warehouse.setRegion(region);
    warehouse.setAddress(address);

    warehouseRepository.save(warehouse);

    return "redirect:/warehouses";
}

@PostMapping("/{id}/delete")
public String delete(@PathVariable Long id, Model model) {

    Warehouse warehouse = warehouseRepository.findById(id).orElseThrow();

    boolean usedInInventory = inventoryRepository.existsByWarehouse(warehouse);
    boolean usedInSales = saleRepository.existsByWarehouse(warehouse);

    if (usedInInventory || usedInSales) {
        model.addAttribute("warehouses", warehouseRepository.findAll());
        model.addAttribute("errorMessage", "재고 또는 판매 기록이 있는 창고는 삭제할 수 없습니다.");
        return "warehouse/list";
    }

    warehouseRepository.delete(warehouse);
    return "redirect:/warehouses";
}
}
