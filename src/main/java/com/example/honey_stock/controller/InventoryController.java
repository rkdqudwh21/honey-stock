package com.example.honey_stock.controller;

import com.example.honey_stock.entity.Inventory;
import com.example.honey_stock.repository.InventoryRepository;
import com.example.honey_stock.repository.ProductRepository;
import com.example.honey_stock.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

 @GetMapping
public String list(@RequestParam(required = false) String keyword, Model model) {

    if (keyword != null && !keyword.isBlank()) {
        model.addAttribute("inventories",
                inventoryRepository.findByProduct_NameContainingOrWarehouse_NameContainingOrWarehouse_RegionContaining(
                        keyword, keyword, keyword
                ));
    } else {
        model.addAttribute("inventories", inventoryRepository.findAll());
    }

    model.addAttribute("keyword", keyword);

    return "inventory/list";
}
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "inventory/form";
    }

    @PostMapping
public String save(@RequestParam Long productId,
                   @RequestParam Long warehouseId,
                   @RequestParam int quantity) {

    Inventory inventory = new Inventory();
    inventory.setProduct(productRepository.findById(productId).orElseThrow());
    inventory.setWarehouse(warehouseRepository.findById(warehouseId).orElseThrow());
    inventory.setQuantity(quantity);

    inventoryRepository.save(inventory);

    return "redirect:/inventories";
}
@GetMapping("/{id}/edit")
public String editForm(@PathVariable Long id, Model model) {
    Inventory inventory = inventoryRepository.findById(id).orElseThrow();
    model.addAttribute("inventory", inventory);
    return "inventory/edit";
}

@PostMapping("/{id}/edit")
public String update(@PathVariable Long id,
                     @RequestParam int quantity) {

    Inventory inventory = inventoryRepository.findById(id).orElseThrow();
    inventory.setQuantity(quantity);
    inventoryRepository.save(inventory);

    return "redirect:/inventories";
}
}