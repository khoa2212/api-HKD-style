package com.example.apidemo.cart.repository;

import com.example.apidemo.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    public Optional<Cart> findByUserId(UUID userId);
}
