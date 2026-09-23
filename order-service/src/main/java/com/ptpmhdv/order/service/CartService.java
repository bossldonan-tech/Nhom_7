package com.ptpmhdv.order.service;

import com.ptpmhdv.common.exception.BadRequestException;
import com.ptpmhdv.order.client.ProductClient;
import com.ptpmhdv.order.client.ProductDto;
import com.ptpmhdv.order.dto.CartItemRequest;
import com.ptpmhdv.order.dto.CartItemResponse;
import com.ptpmhdv.order.entity.CartItem;
import com.ptpmhdv.order.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public CartService(CartItemRepository cartItemRepository, ProductClient productClient) {
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
    }

    public List<CartItemResponse> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId).stream().map(CartItemResponse::from).toList();
    }

    public List<CartItemResponse> addToCart(Long userId, CartItemRequest request) {
        ProductDto product = productClient.findById(request.getProductId());
        if (product == null) {
            throw new BadRequestException("San pham khong ton tai");
        }
        if (product.stockQuantity() < request.getQuantity()) {
            throw new BadRequestException("San pham khong du hang ton kho");
        }

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUserId(userId);
                    newItem.setProductId(product.id());
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setProductName(product.name());
        item.setProductImage(product.imageUrl());
        item.setPrice(product.price());
        item.setQuantity(item.getQuantity() + request.getQuantity());
        cartItemRepository.save(item);

        return getCart(userId);
    }

    public List<CartItemResponse> updateQuantity(Long userId, Long productId, int quantity) {
        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new BadRequestException("San pham khong co trong gio hang"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return getCart(userId);
    }

    public List<CartItemResponse> removeItem(Long userId, Long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
        return getCart(userId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
