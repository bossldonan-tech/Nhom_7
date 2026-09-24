package com.ptpmhdv.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "orderId khong duoc de trong")
    private Long orderId;

    private String orderCode;

    @NotNull(message = "amount khong duoc de trong")
    private BigDecimal amount;

    @NotNull(message = "method khong duoc de trong")
    private String method;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
