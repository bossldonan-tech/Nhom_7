package com.ptpmhdv.order.controller;

import com.ptpmhdv.common.security.CurrentUser;
import com.ptpmhdv.order.dto.CheckoutRequest;
import com.ptpmhdv.order.dto.OrderResponse;
import com.ptpmhdv.order.entity.OrderStatus;
import com.ptpmhdv.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkout(CurrentUser.get().userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> myOrders() {
        return ResponseEntity.ok(orderService.myOrders(CurrentUser.get().userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> myOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.myOrder(CurrentUser.get().userId(), id));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> allOrders() {
        return ResponseEntity.ok(orderService.allOrders());
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
