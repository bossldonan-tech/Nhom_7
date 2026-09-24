package com.ptpmhdv.payment.service;

import com.ptpmhdv.common.security.CurrentUser;
import com.ptpmhdv.payment.dto.PaymentRequest;
import com.ptpmhdv.payment.dto.PaymentResponse;
import com.ptpmhdv.payment.entity.Payment;
import com.ptpmhdv.payment.entity.PaymentStatus;
import com.ptpmhdv.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository, NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
    }

    /**
     * Xu ly (gia lap) thanh toan cho mot don hang. COD se cho trang thai PENDING
     * (thu tien khi giao hang), cac phuong thuc con lai (BANK_TRANSFER, CREDIT_CARD, ...)
     * duoc coi la thanh cong ngay lap tuc de phuc vu demo.
     */
    public PaymentResponse create(PaymentRequest request) {
        Long userId = CurrentUser.get().userId();

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setOrderCode(request.getOrderCode());
        payment.setUserId(userId);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setTransactionCode("TXN" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());

        boolean isCod = "COD".equalsIgnoreCase(request.getMethod());
        payment.setStatus(isCod ? PaymentStatus.PENDING : PaymentStatus.SUCCESS);

        Payment saved = paymentRepository.save(payment);

        if (isCod) {
            notificationService.notify(userId, "Dat hang thanh cong",
                    "Don hang " + request.getOrderCode() + " da duoc dat. Vui long thanh toan khi nhan hang (COD).",
                    "ORDER");
        } else {
            notificationService.notify(userId, "Thanh toan thanh cong",
                    "Don hang " + request.getOrderCode() + " da duoc thanh toan thanh cong voi so tien "
                            + request.getAmount() + " VND.",
                    "PAYMENT");
        }

        return PaymentResponse.from(saved);
    }

    public List<PaymentResponse> byOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream().map(PaymentResponse::from).toList();
    }

    public List<PaymentResponse> myPayments(Long userId) {
        return paymentRepository.findByUserId(userId).stream().map(PaymentResponse::from).toList();
    }
}
