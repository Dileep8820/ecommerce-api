package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.entity.OrderStatus;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Customer: Place an order
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request,
                                                    Authentication auth) {
        return new ResponseEntity<>(orderService.placeOrder(request, auth), HttpStatus.CREATED);
    }

    // Customer: View own orders
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> viewOwnOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.viewOwnOrders(auth));
    }

    // Admin: View all orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> viewAllOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.viewAllOrders(auth));
    }

    // Admin: Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
                                                           @RequestParam OrderStatus status,
                                                           Authentication auth) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, auth));
    }
}
