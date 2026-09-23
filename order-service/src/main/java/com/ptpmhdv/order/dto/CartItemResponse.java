package com.ptpmhdv.order.dto;

import com.ptpmhdv.order.entity.CartItem;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String productImage,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getProductImage(),
                item.getPrice(),
                item.getQuantity(),
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
