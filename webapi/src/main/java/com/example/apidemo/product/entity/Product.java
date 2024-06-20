package com.example.apidemo.product.entity;

import com.example.apidemo.base.BaseEntity;
import com.example.apidemo.enums.CategoryEnum;
import com.example.apidemo.enums.StatusEnum;
import com.example.apidemo.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column
    private Integer stock;

    @Column
    private Integer sales;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private String attachment;

    @OneToMany(mappedBy = "product")
    List<Review> reviews;
}
