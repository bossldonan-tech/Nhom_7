package com.ptpmhdv.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "productId khong duoc de trong")
    private Long productId;

    @NotNull(message = "quantity khong duoc de trong")
    @Min(value = 1, message = "So luong phai >= 1")
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
