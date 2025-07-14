package com.microservices.order.repository;

import com.microservices.order.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    boolean deleteByUserIdAndProductId(String userId, String productId);

    List<CartItem> findByUserId(String userId);

    void deleteByUserId(String userId);
}
