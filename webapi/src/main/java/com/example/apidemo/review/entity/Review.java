package com.example.apidemo.review.entity;

import com.example.apidemo.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private UUID productId;
    @Column
    private int rating;
    @Column
    private String content;
}
