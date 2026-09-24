package com.ptpmhdv.payment.dto;

import com.ptpmhdv.payment.entity.Payment;
import com.ptpmhdv.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        String orderCode,
        BigDecimal amount,
        String method,
        PaymentStatus status,
        String transactionCode,
        LocalDateTime createdAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getOrderCode(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionCode(),
                payment.getCreatedAt()
        );
    }
}
