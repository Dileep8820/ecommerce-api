package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // Customer: Place an order
    public OrderResponse placeOrder(OrderRequest request, Authentication auth) {
        checkCustomer(auth);
        String username = auth.getName();

        List<OrderItem> items = request.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Reduce stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemRequest.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUsername(username);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(items);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    // Customer: View own orders
    public List<OrderResponse> viewOwnOrders(Authentication auth) {
        checkCustomer(auth);
        String username = auth.getName();
        return orderRepository.findByUsername(username)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Admin: View all orders
    public List<OrderResponse> viewAllOrders(Authentication auth) {
        checkAdmin(auth);
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Admin: Update order status
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Authentication auth) {
        checkAdmin(auth);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return mapToResponse(orderRepository.save(order));
    }

    // -----------------------------
    private void checkCustomer(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_CUSTOMER.name()))) {
            throw new SecurityException("Access denied: Customers only");
        }
    }

    private void checkAdmin(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
            throw new SecurityException("Access denied: Admins only");
        }
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUsername(order.getUsername());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotal(order.getTotal());

        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> {
                    OrderResponse.OrderItemResponse item = new OrderResponse.OrderItemResponse();
                    item.setProductId(i.getProductId());
                    item.setProductName(i.getProductName());
                    item.setPrice(i.getPrice());
                    item.setQuantity(i.getQuantity());
                    return item;
                }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
