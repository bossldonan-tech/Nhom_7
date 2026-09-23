package com.ptpmhdv.order.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {

    @NotBlank(message = "Ho ten nguoi nhan khong duoc de trong")
    private String shippingName;

    @NotBlank(message = "So dien thoai khong duoc de trong")
    private String shippingPhone;

    @NotBlank(message = "Dia chi giao hang khong duoc de trong")
    private String shippingAddress;

    @NotBlank(message = "Phuong thuc thanh toan khong duoc de trong")
    private String paymentMethod;

    public String getShippingName() { return shippingName; }
    public void setShippingName(String shippingName) { this.shippingName = shippingName; }
    public String getShippingPhone() { return shippingPhone; }
    public void setShippingPhone(String shippingPhone) { this.shippingPhone = shippingPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
