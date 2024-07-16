package com.example.apidemo.wishlist.repository;

import com.example.apidemo.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    public List<Wishlist> findAllByUserId(UUID userId);

    public Optional<Wishlist> findByUserIdAndProductId(UUID userId, UUID productId);
}
