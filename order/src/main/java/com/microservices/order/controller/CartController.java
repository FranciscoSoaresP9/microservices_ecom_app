package com.microservices.order.controller;

import com.microservices.order.dto.CartItemRequest;
import com.microservices.order.dto.CartItemResponse;
import com.microservices.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping()
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request) {
        return service.addToCart(userId, request) ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                ResponseEntity.badRequest().body("Product out of stock or User not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId) {
        return service.deleteItemFromCart(userId, productId) ?
                new ResponseEntity<>(HttpStatus.OK) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(service.getCartResponse(userId));
    }
}
