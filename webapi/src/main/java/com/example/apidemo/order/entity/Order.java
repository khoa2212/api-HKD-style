package com.example.apidemo.order.entity;

import com.example.apidemo.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(nullable = false)
    private String fullName;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private String city;
    @Column(nullable = false)
    private UUID userId;
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;
}
