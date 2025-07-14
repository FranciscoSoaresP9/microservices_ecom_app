package com.microservices.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String userId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
}
