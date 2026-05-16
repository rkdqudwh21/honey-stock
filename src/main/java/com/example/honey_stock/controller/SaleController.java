package com.example.honey_stock.controller;

import com.example.honey_stock.entity.Inventory;
import com.example.honey_stock.entity.Product;
import com.example.honey_stock.entity.Sale;
import com.example.honey_stock.entity.Warehouse;
import com.example.honey_stock.repository.InventoryRepository;
import com.example.honey_stock.repository.ProductRepository;
import com.example.honey_stock.repository.SaleRepository;
import com.example.honey_stock.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleController {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;

@GetMapping
public String list(@RequestParam(required = false) String keyword,
                   @RequestParam(required = false) String date,
                   Model model) {

    List<Sale> sales;

    if (date != null && !date.isBlank()) {

        LocalDateTime start = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(date + "T23:59:59");

        sales = saleRepository.findBySaleDateBetween(start, end);

    } else if (keyword != null && !keyword.isBlank()) {

        sales = saleRepository
                .findByProduct_NameContainingOrWarehouse_NameContainingOrWarehouse_RegionContainingOrMemoContaining(
                        keyword, keyword, keyword, keyword
                );

    } else {

        sales = saleRepository.findAll();
    }

    int totalQuantity = sales.stream()
            .mapToInt(Sale::getQuantity)
            .sum();

    int totalAmount = sales.stream()
            .mapToInt(Sale::getTotalPrice)
            .sum();

    model.addAttribute("sales", sales);
    model.addAttribute("keyword", keyword);
    model.addAttribute("date", date);
    model.addAttribute("totalQuantity", totalQuantity);
    model.addAttribute("totalAmount", totalAmount);

    return "sale/list";
}

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "sale/form";
    }

    @PostMapping
    public String save(@RequestParam Long productId,
                       @RequestParam Long warehouseId,
                       @RequestParam int quantity,
                       @RequestParam(required = false) String memo,
                       Model model) {

        Product product = productRepository.findById(productId).orElseThrow();
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow();

        Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 창고 재고가 없습니다."));

        if (inventory.getQuantity() < quantity) {
            model.addAttribute("errorMessage", "재고가 부족합니다. 현재 재고: " + inventory.getQuantity());
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("warehouses", warehouseRepository.findAll());
            return "sale/form";
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setWarehouse(warehouse);
        sale.setQuantity(quantity);
        sale.setTotalPrice(product.getPrice() * quantity);
        sale.setSaleDate(LocalDateTime.now());
        sale.setMemo(memo);

        saleRepository.save(sale);

        return "redirect:/sales";
    }
    @PostMapping("/{id}/cancel")
public String cancel(@PathVariable Long id) {

    Sale sale = saleRepository.findById(id).orElseThrow();

    Inventory inventory = inventoryRepository
            .findByProductAndWarehouse(sale.getProduct(), sale.getWarehouse())
            .orElseThrow(() -> new IllegalArgumentException("해당 재고 정보를 찾을 수 없습니다."));

    inventory.setQuantity(inventory.getQuantity() + sale.getQuantity());
    inventoryRepository.save(inventory);

    saleRepository.delete(sale);

    return "redirect:/sales";
}
}