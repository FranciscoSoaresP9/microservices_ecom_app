package com.microservices.order.dto;

import com.microservices.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
}
