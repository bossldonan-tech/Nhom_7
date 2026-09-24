package com.ptpmhdv.product.controller;

import com.ptpmhdv.product.dto.ProductRequest;
import com.ptpmhdv.product.dto.ProductResponse;
import com.ptpmhdv.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(productService.search(keyword, categoryId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint noi bo, duoc order-service goi qua Feign khi checkout (khong yeu cau quyen ADMIN) ---

    @GetMapping("/internal/batch")
    public ResponseEntity<List<ProductResponse>> findByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(productService.findByIds(ids));
    }

    @PutMapping("/internal/{id}/decrease-stock")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.decreaseStock(id, quantity);
        return ResponseEntity.noContent().build();
    }
}
