package com.example.apidemo.review.entity;

import com.example.apidemo.base.BaseEntity;
import com.example.apidemo.product.entity.Product;
import jakarta.persistence.*;
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
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Column
    private int rating;
    @Column
    private String content;
    @Column
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
