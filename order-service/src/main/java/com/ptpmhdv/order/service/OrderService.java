package com.ptpmhdv.order.service;

import com.ptpmhdv.common.exception.BadRequestException;
import com.ptpmhdv.common.exception.ResourceNotFoundException;
import com.ptpmhdv.order.client.PaymentClient;
import com.ptpmhdv.order.client.PaymentRequestDto;
import com.ptpmhdv.order.client.PaymentResponseDto;
import com.ptpmhdv.order.client.ProductClient;
import com.ptpmhdv.order.client.ProductDto;
import com.ptpmhdv.order.dto.CheckoutRequest;
import com.ptpmhdv.order.dto.OrderResponse;
import com.ptpmhdv.order.entity.CartItem;
import com.ptpmhdv.order.entity.Order;
import com.ptpmhdv.order.entity.OrderItem;
import com.ptpmhdv.order.entity.OrderStatus;
import com.ptpmhdv.order.repository.CartItemRepository;
import com.ptpmhdv.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                         ProductClient productClient, PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Gio hang dang trong");
        }

        List<Long> productIds = cartItems.stream().map(CartItem::getProductId).toList();
        Map<Long, ProductDto> products = productClient.findByIds(productIds).stream()
                .collect(Collectors.toMap(ProductDto::id, p -> p));

        for (CartItem item : cartItems) {
            ProductDto product = products.get(item.getProductId());
            if (product == null) {
                throw new BadRequestException("San pham '" + item.getProductName() + "' khong con ton tai");
            }
            if (product.stockQuantity() < item.getQuantity()) {
                throw new BadRequestException("San pham '" + product.name() + "' khong du hang ton kho");
            }
        }

        Order order = new Order();
        order.setOrderCode("DH" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setShippingName(request.getShippingName());
        order.setShippingPhone(request.getShippingPhone());
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            ProductDto product = products.get(cartItem.getProductId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.id());
            orderItem.setProductName(product.name());
            orderItem.setProductImage(product.imageUrl());
            orderItem.setPrice(product.price());
            orderItem.setQuantity(cartItem.getQuantity());
            order.addItem(orderItem);

            total = total.add(product.price().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            productClient.decreaseStock(cartItem.getProductId(), cartItem.getQuantity());
        }

        try {
            PaymentResponseDto payment = paymentClient.create(
                    new PaymentRequestDto(saved.getId(), saved.getOrderCode(), saved.getTotalAmount(), saved.getPaymentMethod())
            );
            if (payment != null && "SUCCESS".equalsIgnoreCase(payment.status())) {
                saved.setStatus(OrderStatus.PAID);
                orderRepository.save(saved);
            }
        } catch (Exception ex) {
            // Thanh toan that bai/khong san sang khong lam huy don hang: don van o trang thai PENDING
            // de nguoi dung/quan tri vien xu ly lai (vi du COD hoac thu thanh toan lai sau).
        }

        cartItemRepository.deleteByUserId(userId);

        return OrderResponse.from(saved);
    }

    public List<OrderResponse> myOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(OrderResponse::from).toList();
    }

    public OrderResponse myOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay don hang"));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> allOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay don hang"));
        order.setStatus(status);
        return OrderResponse.from(orderRepository.save(order));
    }
}
