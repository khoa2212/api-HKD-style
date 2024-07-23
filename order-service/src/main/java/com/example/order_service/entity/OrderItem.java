package com.example.order_service.entity;

import com.example.order_service.base.BaseEntity;
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
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
