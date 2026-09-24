package com.ptpmhdv.payment.controller;

import com.ptpmhdv.common.security.CurrentUser;
import com.ptpmhdv.payment.dto.PaymentRequest;
import com.ptpmhdv.payment.dto.PaymentResponse;
import com.ptpmhdv.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> byOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.byOrder(orderId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PaymentResponse>> myPayments() {
        return ResponseEntity.ok(paymentService.myPayments(CurrentUser.get().userId()));
    }
}
