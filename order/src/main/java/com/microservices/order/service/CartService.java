package com.microservices.order.service;

import com.microservices.order.dto.CartItemRequest;
import com.microservices.order.dto.CartItemResponse;
import com.microservices.order.model.CartItem;
import com.microservices.order.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository repository;


    public boolean addToCart(String userId, CartItemRequest request) {
        var existingCartItem = getOptionCartItem(userId, request.getProductId());

        var cartItem = existingCartItem
                .map(c -> {
                    c.setQuantity(c.getQuantity() + request.getQuantity());
                    return c;
                })
                .orElse(
                        new CartItem()
                                .withPrice(BigDecimal.ZERO)
                                .withQuantity(request.getQuantity())
                                .withUserId(userId)
                                .withProductId(request.getProductId())
                );

        repository.save(cartItem);

        return true;
    }

    private Optional<CartItem> getOptionCartItem(String userId, String productId) {
        return repository.findByUserIdAndProductId(userId, productId);
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        return repository.deleteByUserIdAndProductId(userId, productId);
    }

    public List<CartItemResponse> getCartResponse(String userId) {
        return getCart(userId).stream()
                .map(this::mapCartItemToResponse)
                .collect(Collectors.toList());
    }

    public List<CartItem> getCart(String userId) {
        return repository.findByUserId(userId);
    }

    private CartItemResponse mapCartItemToResponse(CartItem cartItem) {
        return new CartItemResponse()
                .withProductId(cartItem.getProductId())
                .withUserId(cartItem.getUserId())
                .withQuantity(cartItem.getQuantity())
                .withPrice(BigDecimal.ZERO);
    }

    public void clearCart(String userId) {
        repository.deleteByUserId(userId);
    }
}
