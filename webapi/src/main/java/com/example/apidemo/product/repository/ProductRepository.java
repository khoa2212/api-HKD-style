package com.example.apidemo.product.repository;

import com.example.apidemo.enums.StatusEnum;
import com.example.apidemo.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT p FROM Product p WHERE p.status = :status",
            countQuery = "SELECT count(p) FROM Product p WHERE p.status = :status")
    Page<Product> findByStatus(StatusEnum status, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.status = :status and p.sales > 0",
            countQuery = "SELECT count(p) FROM Product p WHERE p.status = :status and p.sales > 0")
    Page<Product> findBySalesAndStatus(StatusEnum status, Pageable pageable);

    Optional<Product> findByStatusAndId(StatusEnum statusEnum, UUID id);
}
