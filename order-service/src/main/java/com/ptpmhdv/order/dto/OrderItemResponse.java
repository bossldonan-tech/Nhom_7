package com.ptpmhdv.order.dto;

import com.ptpmhdv.order.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        String productImage,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getProductImage(),
                item.getPrice(),
                item.getQuantity(),
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
