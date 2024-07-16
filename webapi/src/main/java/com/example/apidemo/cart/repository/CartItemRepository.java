package com.example.apidemo.cart.repository;

import com.example.apidemo.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    public Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
}
