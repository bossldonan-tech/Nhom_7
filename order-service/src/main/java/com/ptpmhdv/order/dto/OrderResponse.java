package com.ptpmhdv.order.dto;

import com.ptpmhdv.order.entity.Order;
import com.ptpmhdv.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderCode,
        BigDecimal totalAmount,
        OrderStatus status,
        String shippingName,
        String shippingPhone,
        String shippingAddress,
        String paymentMethod,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getShippingName(),
                order.getShippingPhone(),
                order.getShippingAddress(),
                order.getPaymentMethod(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
