package com.microservices.order.service;

import com.microservices.order.dto.OrderItemDTO;
import com.microservices.order.dto.OrderResponse;
import com.microservices.order.model.CartItem;
import com.microservices.order.model.Order;
import com.microservices.order.model.OrderItem;
import com.microservices.order.model.OrderStatus;
import com.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderResponse createOrder(String userId) {
        var items = cartService.getCart(userId);

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        var orderItems = items.stream()
                .map(this::mapCartItemToOrderItem)
                .collect(Collectors.toList());

        var order = new Order()
                .withUserId(userId)
                .withStatus(OrderStatus.CONFIRMED)
                .withTotalAmount(getTotalPrice(items))
                .withItems(orderItems);

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        cartService.clearCart(userId);
        orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    private BigDecimal getTotalPrice(List<CartItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItem mapCartItemToOrderItem(CartItem item) {
        return new OrderItem()
                .withProductId(item.getProductId())
                .withQuantity(item.getQuantity())
                .withPrice(item.getPrice());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse()
                .withOrderId(order.getId())
                .withItems(order.getItems().stream().map(this::mapOrderItemToDTO).collect(Collectors.toList()))
                .withStatus(order.getStatus())
                .withTotalAmount(order.getTotalAmount())
                .withCreatedAt(order.getCreatedAt());
    }

    private OrderItemDTO mapOrderItemToDTO(OrderItem orderItem) {
        return new OrderItemDTO()
                .withId(orderItem.getId())
                .withPrice(orderItem.getPrice())
                .withQuantity(orderItem.getQuantity())
                .withProductId(orderItem.getProductId())
                .withSubTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
    }

}
