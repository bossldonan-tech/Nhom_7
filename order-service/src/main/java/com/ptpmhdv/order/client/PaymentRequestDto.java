package com.ptpmhdv.order.client;

import java.math.BigDecimal;

public record PaymentRequestDto(Long orderId, String orderCode, BigDecimal amount, String method) {
}
