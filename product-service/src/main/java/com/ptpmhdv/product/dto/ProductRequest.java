package com.ptpmhdv.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Ten san pham khong duoc de trong")
    private String name;

    private String description;

    @NotNull(message = "Gia khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = true, message = "Gia phai >= 0")
    private BigDecimal price;

    @NotNull(message = "So luong ton khong duoc de trong")
    @Min(value = 0, message = "So luong ton phai >= 0")
    private Integer stockQuantity;

    private String imageUrl;
    private String brand;
    private Long categoryId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
