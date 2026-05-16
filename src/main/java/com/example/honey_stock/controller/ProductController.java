package com.example.honey_stock.controller;

import com.example.honey_stock.entity.Product;
import com.example.honey_stock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.honey_stock.repository.InventoryRepository;
import com.example.honey_stock.repository.SaleRepository;
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;

   @GetMapping
public String list(@RequestParam(required = false) String keyword, Model model) {

    if (keyword != null && !keyword.isBlank()) {
        model.addAttribute("products",
                productRepository.findByNameContainingOrSizeContainingOrDescriptionContaining(
                        keyword, keyword, keyword
                ));
    } else {
        model.addAttribute("products", productRepository.findAll());
    }

    model.addAttribute("keyword", keyword);
    return "product/list";
}

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @PostMapping
    public String save(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/{id}/edit")
public String editForm(@PathVariable Long id, Model model) {
    Product product = productRepository.findById(id).orElseThrow();
    model.addAttribute("product", product);
    return "product/edit";
}

@PostMapping("/{id}/edit")
public String update(@PathVariable Long id,
                     @RequestParam String name,
                     @RequestParam String size,
                     @RequestParam int price,
                     @RequestParam(required = false) String description) {

    Product product = productRepository.findById(id).orElseThrow();

    product.setName(name);
    product.setSize(size);
    product.setPrice(price);
    product.setDescription(description);

    productRepository.save(product);

    return "redirect:/products";
}

@PostMapping("/{id}/delete")
public String delete(@PathVariable Long id, Model model) {

    Product product = productRepository.findById(id).orElseThrow();

    boolean usedInInventory = inventoryRepository.existsByProduct(product);
    boolean usedInSales = saleRepository.existsByProduct(product);

    if (usedInInventory || usedInSales) {

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("errorMessage", "재고 또는 판매 기록이 있는 상품은 삭제할 수 없습니다.");

        return "product/list";
    }

    productRepository.delete(product);

    return "redirect:/products";
}
}