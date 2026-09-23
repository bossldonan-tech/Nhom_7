package com.ptpmhdv.order.client;

public record PaymentResponseDto(Long id, Long orderId, String status, String transactionCode) {
}
