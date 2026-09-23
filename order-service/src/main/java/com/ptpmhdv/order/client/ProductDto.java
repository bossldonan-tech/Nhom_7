package com.ptpmhdv.order.client;

import java.math.BigDecimal;

/** DTO khop voi ProductResponse ben product-service (chi lay cac truong can dung). */
public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        String brand,
        Long categoryId,
        String categoryName
) {
}
