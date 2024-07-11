package com.example.apidemo.cart.entity;

import com.example.apidemo.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;
    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;
}
