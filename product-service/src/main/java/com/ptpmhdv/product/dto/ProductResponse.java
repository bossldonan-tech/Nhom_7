package com.ptpmhdv.product.dto;

import com.ptpmhdv.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        String brand,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getImageUrl(),
                p.getBrand(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getCreatedAt()
        );
    }
}
