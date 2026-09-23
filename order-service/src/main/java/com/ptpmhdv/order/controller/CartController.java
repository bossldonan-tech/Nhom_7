package com.ptpmhdv.order.controller;

import com.ptpmhdv.common.security.CurrentUser;
import com.ptpmhdv.order.dto.CartItemRequest;
import com.ptpmhdv.order.dto.CartItemResponse;
import com.ptpmhdv.order.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart() {
        return ResponseEntity.ok(cartService.getCart(CurrentUser.get().userId()));
    }

    @PostMapping
    public ResponseEntity<List<CartItemResponse>> add(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(CurrentUser.get().userId(), request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<List<CartItemResponse>> updateQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(CurrentUser.get().userId(), productId, quantity));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<List<CartItemResponse>> remove(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(CurrentUser.get().userId(), productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        cartService.clearCart(CurrentUser.get().userId());
        return ResponseEntity.noContent().build();
    }
}
