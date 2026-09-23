package com.ptpmhdv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/internal/batch")
    List<ProductDto> findByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/api/products/{id}")
    ProductDto findById(@PathVariable("id") Long id);

    @PutMapping("/api/products/internal/{id}/decrease-stock")
    void decreaseStock(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);
}
