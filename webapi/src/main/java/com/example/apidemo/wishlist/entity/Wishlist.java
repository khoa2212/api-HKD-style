package com.example.apidemo.wishlist.entity;

import com.example.apidemo.base.BaseEntity;
import com.example.apidemo.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "wishlists")
public class Wishlist extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
