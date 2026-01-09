package com.example.ecommerce.dto;

import com.example.ecommerce.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private String username;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private double total;

    @Getter
    @Setter
    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private double price;
        private int quantity;
    }
}
